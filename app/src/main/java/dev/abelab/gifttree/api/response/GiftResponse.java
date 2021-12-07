package dev.abelab.gifttree.api.response;

import lombok.*;

/**
 * ギフト情報レスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GiftResponse {

    /**
     * ギフトID
     */
    Integer id;

    /**
     * ギフト名
     */
    String name;

    /**
     * 説明文
     */
    String description;

    /**
     * ギフト数
     */
    Integer quantity;

    /**
     * URL
     */
    String url;

}
