package dev.abelab.gifttree.api.response;

import lombok.*;

/**
 * ユーザランクレスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRankResponse {

    /**
     * 順位
     */
    Integer rank;

    /**
     * スコア
     */
    Integer score;

    /**
     * ユーザ
     */
    UserResponse user;

}
