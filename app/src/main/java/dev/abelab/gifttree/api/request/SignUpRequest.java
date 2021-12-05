package dev.abelab.gifttree.api.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.*;

/**
 * サインアップリクエスト
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequest {

    /**
     * ユーザ名
     */
    @NotNull
    @Size(max = 100)
    String name;

    /**
     * メールアドレス
     */
    @NotNull
    @Size(max = 255)
    String email;

    /**
     * パスワード
     */
    @NotNull
    @Size(max = 255)
    String password;

    /**
     * ユーザアイコン(Base64)
     */
    String icon;

}
