package dev.abelab.gifttree.api.response;

import java.util.Date;

import lombok.*;

/**
 * 通知レスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {

    /**
     * 通知ID
     */
    Integer id;

    /**
     * タイトル
     */
    String name;

    /**
     * 通知日
     */
    Date createdAt;

}
