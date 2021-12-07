package dev.abelab.gifttree.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import dev.abelab.gifttree.db.entity.UserGift;
import dev.abelab.gifttree.db.entity.UserGiftExample;
import dev.abelab.gifttree.db.mapper.UserGiftMapper;

@RequiredArgsConstructor
@Repository
public class UserGiftRepository {

    private final UserGiftMapper userGiftMapper;

    /**
     * ユーザギフトを作成
     *
     * @param userGift ユーザギフト
     *
     * @return ユーザギフトID
     */
    public int insert(final UserGift userGift) {
        return this.userGiftMapper.insertSelective(userGift);
    }

}
