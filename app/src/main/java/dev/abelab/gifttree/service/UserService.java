package dev.abelab.gifttree.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.net.util.Base64;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

import lombok.*;
import dev.abelab.gifttree.api.response.UserResponse;
import dev.abelab.gifttree.api.response.NotificationResponse;
import dev.abelab.gifttree.api.response.NotificationsResponse;
import dev.abelab.gifttree.api.request.LoginUserUpdateRequest;
import dev.abelab.gifttree.db.entity.User;
import dev.abelab.gifttree.client.CloudStorageClient;
import dev.abelab.gifttree.exception.ConflictException;
import dev.abelab.gifttree.exception.ErrorCode;
import dev.abelab.gifttree.model.FileModel;
import dev.abelab.gifttree.repository.UserRepository;
import dev.abelab.gifttree.repository.NotificationRepository;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;

    private final NotificationRepository notificationRepository;

    private final ModelMapper modelMapper;

    private final CloudStorageClient cloudStorageClient;

    /**
     * ユーザリストを取得
     *
     * @param loginUser ログインユーザ
     *
     * @return ユーザリスト
     */
    @Transactional
    public List<User> getUsers(final User loginUser) {
        // ユーザリストの取得
        return this.userRepository.selectAll();
    }

    /**
     * ログインユーザのプロフィールを取得
     *
     * @param loginUser ログインユーザ
     *
     * @return プロフィールレスポンス
     */
    @Transactional
    public UserResponse getLoginUser(final User loginUser) {
        return this.modelMapper.map(loginUser, UserResponse.class);
    }

    /**
     * ログインユーザを更新
     *
     * @param requestBody ログインユーザ更新リクエスト
     * @param loginUser   ログインユーザ
     */
    @Transactional
    public void updateLoginUser(final LoginUserUpdateRequest requestBody, final User loginUser) {
        // メールアドレスが既に存在するかチェック
        if (this.userRepository.existsByEmail(requestBody.getEmail())) {
            throw new ConflictException(ErrorCode.CONFLICT_EMAIL);
        }

        // アイコンをアップロード
        String iconUrl = null;
        if (requestBody.getIcon() != null) {
            final var file = FileModel.builder().content(Base64.decodeBase64(requestBody.getIcon())).build();
            file.setName("icons/" + file.getName());
            iconUrl = this.cloudStorageClient.uploadFile(file);
        }

        // ログインユーザを更新
        loginUser.setEmail(requestBody.getEmail());
        loginUser.setName(requestBody.getName());
        loginUser.setIconUrl(iconUrl);

        this.userRepository.update(loginUser);
    }

    /**
     * 通知を取得
     *
     * @param loginUser ログインユーザ
     *
     * @return 通知レスポンス
     */
    @Transactional
    public NotificationsResponse getNotifications(final User loginUser) {
        final var notifications = this.notificationRepository.selectByUserId(loginUser.getId());
        final var notificationResponses = notifications.stream()
            .map(notification -> this.modelMapper.map(notification, NotificationResponse.class)).collect(Collectors.toList());

        return new NotificationsResponse(notificationResponses);
    }

}
