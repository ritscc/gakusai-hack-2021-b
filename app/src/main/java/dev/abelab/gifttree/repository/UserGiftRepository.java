package dev.abelab.gifttree.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import dev.abelab.gifttree.db.entity.UserGift;
import dev.abelab.gifttree.db.entity.UserGiftExample;
import dev.abelab.gifttree.db.mapper.UserGiftMapper;
import dev.abelab.gifttree.exception.ErrorCode;
import dev.abelab.gifttree.exception.NotFoundException;

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

    /**
     * 主キーからユーザギフトを検索
     *
     * @param userId ユーザID
     * @param giftId ギフトID
     *
     * @return ユーザギフト
     */
    public UserGift selectByPrimaryKey(final int userId, final int giftId) {
        return Optional.ofNullable(this.userGiftMapper.selectByPrimaryKey(userId, giftId)) //
            .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_USER_GIFT));
    }

    /**
     * 主キーの存在確認
     *
     * @param userId ユーザID
     * @param giftId ギフトID
     *
     * @return 主キーが存在するか
     */
    public boolean existsByPrimaryKey(final int userId, final int giftId) {
        try {
            this.selectByPrimaryKey(userId, giftId);
            return true;
        } catch (NotFoundException e) {
            return false;
        }
    }

    /**
     * ユーザIDからギフトを取得
     *
     * @param userId
     *
     * @return ギフト一覧
     */
    public List<UserGift> selectByUserId(final int userId) {
        final var userGiftExample = new UserGiftExample();
        userGiftExample.createCriteria().andUserIdEqualTo(userId);
        return this.userGiftMapper.selectByExample(userGiftExample);
    }

}
