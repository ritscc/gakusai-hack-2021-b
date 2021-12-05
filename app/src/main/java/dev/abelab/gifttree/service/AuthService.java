package dev.abelab.gifttree.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.*;
import dev.abelab.gifttree.api.request.LoginRequest;
import dev.abelab.gifttree.api.response.AccessTokenResponse;
import dev.abelab.gifttree.repository.UserRepository;
import dev.abelab.gifttree.util.AuthUtil;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;

    private final AuthUtil authUtil;

    /**
     * ログイン処理
     *
     * @param requestBody ログインリクエスト
     *
     * @return アクセストークンレスポンス
     */
    @Transactional
    public AccessTokenResponse login(final LoginRequest requestBody) {
        // ユーザ情報を取得
        final var loginUser = this.userRepository.selectByEmail(requestBody.getEmail());

        // パスワードチェック
        this.authUtil.verifyPassword(loginUser, requestBody.getPassword());

        // アクセストークンを発行
        final var credentials = this.authUtil.generateCredentials(loginUser);
        return AccessTokenResponse.builder() //
            .accessToken(credentials) //
            .tokenType("Bearer") //
            .build();
    }

}
