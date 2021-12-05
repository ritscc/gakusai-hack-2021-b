package dev.abelab.gifttree.api.response;

import lombok.*;

/**
 * ユーザ情報レスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    /**
     * ユーザID
     */
    Integer id;

    /**
     * メールアドレス
     */
    String email;

    /**
     * ユーザ名
     */
    String name;

    /**
     * アイコンURL
     */
    String iconUrl;

}
