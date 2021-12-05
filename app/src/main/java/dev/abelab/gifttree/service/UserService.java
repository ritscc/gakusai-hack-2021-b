package dev.abelab.gifttree.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.modelmapper.ModelMapper;

import lombok.*;
import dev.abelab.gifttree.api.response.UserResponse;
import dev.abelab.gifttree.db.entity.User;

@RequiredArgsConstructor
@Service
public class UserService {

    private final ModelMapper modelMapper;

    /**
     * ログインユーザのプロフィールを取得
     *
     * @param loginUser ログインユーザ
     *
     * @return プロフィールレスポンス
     */
    @Transactional
    public UserResponse getLoginUser(final User loginUser) {
        System.out.println(loginUser);
        return this.modelMapper.map(loginUser, UserResponse.class);
    }

}
