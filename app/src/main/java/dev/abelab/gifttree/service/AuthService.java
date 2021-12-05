package dev.abelab.gifttree.service;

import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.net.util.Base64;
import org.modelmapper.ModelMapper;

import lombok.*;
import dev.abelab.gifttree.api.request.LoginRequest;
import dev.abelab.gifttree.api.request.SignUpRequest;
import dev.abelab.gifttree.api.response.AccessTokenResponse;
import dev.abelab.gifttree.db.entity.User;
import dev.abelab.gifttree.model.FileModel;
import dev.abelab.gifttree.repository.UserRepository;
import dev.abelab.gifttree.util.AuthUtil;
import dev.abelab.gifttree.client.CloudStorageClient;
import dev.abelab.gifttree.exception.ErrorCode;
import dev.abelab.gifttree.exception.UnauthorizedException;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final ModelMapper modelMapper;

    private final UserRepository userRepository;

    private final AuthUtil authUtil;

    private final CloudStorageClient cloudStorageClient;

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

    /**
     * サインアップ処理
     *
     * @param requestBody サインアップリクエスト
     *
     * @return アクセストークンレスポンス
     */
    @Transactional
    public AccessTokenResponse signUp(final SignUpRequest requestBody) {
        // 有効なパスワードかチェック
        this.authUtil.validatePassword(requestBody.getPassword());

        // アイコンをアップロード
        String iconUrl = null;
        if (!Objects.isNull(requestBody.getIcon())) {
            final var file = FileModel.builder().content(Base64.decodeBase64(requestBody.getIcon())).build();
            file.setName("icons/" + file.getName());
            iconUrl = this.cloudStorageClient.uploadFile(file);
        }

        // ユーザを作成
        final var user = this.modelMapper.map(requestBody, User.class);
        user.setPassword(this.authUtil.encodePassword(requestBody.getPassword()));
        user.setIconUrl(iconUrl);
        this.userRepository.insert(user);

        // アクセストークンを発行
        final var credentials = this.authUtil.generateCredentials(user);
        return AccessTokenResponse.builder() //
            .accessToken(credentials) //
            .tokenType("Bearer") //
            .build();
    }

    /**
     * ログインユーザを取得
     *
     * @param credentials クレデンシャル
     *
     * @return ログインユーザ
     */
    @Transactional
    public User getLoginUser(final String credentials) {
        // クレデンシャルの構文チェック
        if (!credentials.startsWith("Bearer ")) {
            throw new UnauthorizedException(ErrorCode.INVALID_ACCESS_TOKEN);
        }
        final var userId = this.authUtil.verifyCredentials(credentials.substring(7));

        // ログインユーザを取得
        return this.userRepository.selectById(userId);
    }

}
