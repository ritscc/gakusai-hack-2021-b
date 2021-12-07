package dev.abelab.gifttree.service;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

import lombok.*;
import dev.abelab.gifttree.api.response.GiftResponse;
import dev.abelab.gifttree.api.response.GiftsResponse;
import dev.abelab.gifttree.api.request.GiftShareRequest;
import dev.abelab.gifttree.db.entity.User;
import dev.abelab.gifttree.db.entity.UserGift;
import dev.abelab.gifttree.db.entity.Notification;
import dev.abelab.gifttree.enums.UserGiftTypeEnum;
import dev.abelab.gifttree.repository.GiftRepository;
import dev.abelab.gifttree.repository.UserGiftRepository;
import dev.abelab.gifttree.repository.UserRepository;
import dev.abelab.gifttree.repository.NotificationRepository;
import dev.abelab.gifttree.exception.ErrorCode;
import dev.abelab.gifttree.exception.ConflictException;
import dev.abelab.gifttree.exception.BadRequestException;

@RequiredArgsConstructor
@Service
public class GiftService {

    private final ModelMapper modelMapper;

    private final GiftRepository giftRepository;

    private final UserGiftRepository userGiftRepository;

    private final UserRepository userRepository;

    private final NotificationRepository notificationRepository;

    /**
     * ギフトを獲得
     *
     * @param giftId    ギフトID
     * @param loginUser ログインユーザ
     */
    @Transactional
    public void obtainGift(final int giftId, final User loginUser) {
        // 獲得対象ギフトを取得
        final var gift = this.giftRepository.selectById(giftId);

        if (this.userGiftRepository.existsByPrimaryKey(loginUser.getId(), giftId)) {
            throw new ConflictException(ErrorCode.GIFT_ALREADY_OBTAINED);
        }

        // ユーザギフトを作成
        final var userGift = UserGift.builder() //
            .userId(loginUser.getId()) //
            .giftId(giftId) //
            .quantity(gift.getQuantity()) //
            .type(UserGiftTypeEnum.STORE.getId()) //
            .receivedBy(null) //
            .build();
        this.userGiftRepository.insert(userGift);

        // 通知を作成
        final var notification = Notification.builder() //
            .userId(loginUser.getId()) //
            .title(gift.getName() + "を店頭で受け取りました") //
            .description("") //
            .build();
        this.notificationRepository.insert(notification);
    }

    /**
     * 所持ギフト一覧取得API
     *
     * @param loginUser ログインユーザ
     *
     * @return ギフト一覧レスポンス
     */
    @Transactional
    public GiftsResponse getLoginUserGifts(final User loginUser) {
        // FIXME: N+1問題なので自作SQLが必要（ハッカソンなので許して）
        final var userGifts = this.userGiftRepository.selectByUserId(loginUser.getId());
        final var giftResponses = userGifts.stream().map(userGift -> {
            final var gift = this.giftRepository.selectById(userGift.getGiftId());
            final var giftResponse = this.modelMapper.map(gift, GiftResponse.class);
            giftResponse.setQuantity(userGift.getQuantity());

            return giftResponse;
        }).collect(Collectors.toList());

        return new GiftsResponse(giftResponses);
    }

    /**
     * ギフトおすそわけAPI
     *
     * @param userId      ユーザID
     * @param requestBody ギフトおすそわけリクエスト
     * @param loginUser   ログインユーザ
     */
    @Transactional
    public void shareGift(final int userId, final GiftShareRequest requestBody, final User loginUser) {
        // おすそわけするユーザを取得
        final var user = this.userRepository.selectById(userId);
        // ギフトを取得
        final var giftId = requestBody.getGiftId();
        final var userGift = this.userGiftRepository.selectByPrimaryKey(loginUser.getId(), giftId);

        // ギフト数を減らす
        if (userGift.getQuantity() == 0) {
            throw new BadRequestException(ErrorCode.USER_HAS_NO_GIFT);
        } else if (userGift.getQuantity() == 1) {
            this.userGiftRepository.delete(loginUser.getId(), giftId);
        } else {
            userGift.setQuantity(userGift.getQuantity() - 1);
            this.userGiftRepository.update(userGift);
        }

        // おすそわけされるユーザがギフトを獲得
        if (this.userGiftRepository.existsByPrimaryKey(user.getId(), giftId)) {
            final var updatedUserGift = this.userGiftRepository.selectByPrimaryKey(user.getId(), giftId);
            updatedUserGift.setQuantity(updatedUserGift.getQuantity() + 1);
            this.userGiftRepository.update(updatedUserGift);
        } else {
            final var createdUserGift = UserGift.builder() //
                .userId(user.getId()) //
                .giftId(giftId) //
                .quantity(1) //
                .type(UserGiftTypeEnum.FRIEND.getId()) //
                .receivedBy(loginUser.getId()) //
                .build();
            this.userGiftRepository.insert(createdUserGift);
        }

        // 通知を作成
        final var notifications = Arrays.asList( //
            Notification.builder() //
                .userId(loginUser.getId()) //
                .title(user.getName() + "にギフトを送りました") //
                .description("") //
                .build(), //
            Notification.builder() //
                .userId(user.getId()) //
                .title(loginUser.getName() + "からギフトを受け取りました") //
                .description("") //
                .build() //
        );
        notifications.forEach(this.notificationRepository::insert);
    }

}
