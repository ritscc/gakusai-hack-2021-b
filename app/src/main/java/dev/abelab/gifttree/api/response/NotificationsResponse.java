package dev.abelab.gifttree.api.response;

import java.util.List;

import lombok.*;

/**
 * 通知一覧レスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationsResponse {

    /**
     * 通知一覧
     */
    List<NotificationResponse> notifications;

}
