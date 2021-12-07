package dev.abelab.gifttree.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import dev.abelab.gifttree.db.entity.Notification;
import dev.abelab.gifttree.db.entity.NotificationExample;
import dev.abelab.gifttree.db.mapper.NotificationMapper;

@RequiredArgsConstructor
@Repository
public class NotificationRepository {

    private final NotificationMapper notificationMapper;

    /**
     * ユーザIDから通知を検索
     *
     * @param userId ユーザID
     *
     * @return 通知一覧
     */
    public List<Notification> selectByUserId(final int userId) {
        final var notificationExample = new NotificationExample();
        notificationExample.createCriteria().andUserIdEqualTo(userId);
        return this.notificationMapper.selectByExample(notificationExample);
    }

}
