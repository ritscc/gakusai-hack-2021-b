package dev.abelab.gifttree.service;

import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

import lombok.*;
import dev.abelab.gifttree.api.response.GiftResponse;
import dev.abelab.gifttree.api.response.GiftsResponse;
import dev.abelab.gifttree.db.entity.User;
import dev.abelab.gifttree.db.entity.UserGift;
import dev.abelab.gifttree.enums.UserGiftTypeEnum;
import dev.abelab.gifttree.repository.GiftRepository;
import dev.abelab.gifttree.repository.UserGiftRepository;
import dev.abelab.gifttree.exception.ErrorCode;
import dev.abelab.gifttree.exception.ConflictException;

@RequiredArgsConstructor
@Service
public class GiftService {

    private final ModelMapper modelMapper;

    private final GiftRepository giftRepository;

    private final UserGiftRepository userGiftRepository;

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

}
