package dev.abelab.gifttree.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.*;
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

}
