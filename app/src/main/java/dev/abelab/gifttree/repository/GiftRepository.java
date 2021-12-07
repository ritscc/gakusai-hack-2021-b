package dev.abelab.gifttree.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import dev.abelab.gifttree.db.entity.Gift;
import dev.abelab.gifttree.db.mapper.GiftMapper;
import dev.abelab.gifttree.exception.ErrorCode;
import dev.abelab.gifttree.exception.NotFoundException;

@RequiredArgsConstructor
@Repository
public class GiftRepository {

    private final GiftMapper giftMapper;

    /**
     * IDからギフトを検索
     *
     * @param giftId ギフトID
     *
     * @return ギフト
     */
    public Gift selectById(final int giftId) {
        return Optional.ofNullable(this.giftMapper.selectByPrimaryKey(giftId)) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_GIFT));
    }

}
