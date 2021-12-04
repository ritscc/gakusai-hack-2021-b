package dev.abelab.gifttree.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import dev.abelab.gifttree.db.entity.Notification;
import dev.abelab.gifttree.db.entity.NotificationExample;
import dev.abelab.gifttree.db.mapper.NotificationMapper;

@RequiredArgsConstructor
@Repository
public class NotificationRepository {

    private final NotificationMapper notificationMapper;

}
