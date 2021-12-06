package dev.abelab.gifttree.api.controller;

import dev.abelab.gifttree.api.request.LoginUserUpdateRequest;
import dev.abelab.gifttree.api.response.UsersResponse;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.*;
import lombok.*;
import dev.abelab.gifttree.annotation.Authenticated;
import dev.abelab.gifttree.api.response.UserResponse;
import dev.abelab.gifttree.db.entity.User;
import dev.abelab.gifttree.service.UserService;

import java.util.stream.Collectors;

@Api(tags = "User")
@RestController
@RequestMapping(path = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
@Authenticated
public class UserRestController {

    private final ModelMapper modelMapper;

    private final UserService userService;

    /**
     * ユーザ一覧取得API
     *
     * @param loginUser ログインユーザ
     *
     * @return ユーザ一覧
     */
    @ApiOperation( //
            value = "ユーザ一覧の取得", //
            notes = "ユーザ一覧を取得する。" //
    )
    @ApiResponses( //
            value = { //
                    @ApiResponse(code = 200, message = "取得成功", response = UsersResponse.class), //
                    @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
            } //
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public UsersResponse getUsers( //
                                   @ModelAttribute("LoginUser") final User loginUser //
    ) {
        final var users = this.userService.getUsers(loginUser);
        final var userResponses = users.stream() //
                .map(user -> this.modelMapper.map(user, UserResponse.class)) //
                .collect(Collectors.toList());

        return new UsersResponse(userResponses);
    }

    /**
     * プロフィール取得API
     *
     * @param loginUser ログインユーザ
     *
     * @return ユーザ詳細レスポンス
     */
    @ApiOperation( //
        value = "プロフィール取得", //
        notes = "プロフィールを取得する。" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 200, message = "取得成功", response = UserResponse.class), //
                @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
                @ApiResponse(code = 404, message = "ユーザが存在しない") //
        })
    @GetMapping(value = "/me")
    @ResponseStatus(HttpStatus.OK)
    public UserResponse getLoginUser( //
        @ModelAttribute("LoginUser") final User loginUser //
    ) {
        return this.userService.getLoginUser(loginUser);
    }

    /**
     * ログインユーザ更新API
     *
     * @param loginUser   ログインユーザ
     *
     * @param requestBody ログインユーザ更新リクエスト
     */
    @ApiOperation( //
      value = "ログインユーザの更新", //
      notes = "ログインユーザを更新する。" //
    )
    @ApiResponses( //
      value = { //
        @ApiResponse(code = 200, message = "更新成功"), //
        @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
      } //
    )
    @PutMapping(value = "/me")
    @ResponseStatus(HttpStatus.OK)
    public void updateLoginUser( //
      @ModelAttribute("LoginUser") final User loginUser, //
      @Validated @ApiParam(name = "body", required = true, value = "ユーザ更新情報") @RequestBody final LoginUserUpdateRequest requestBody //
    ) {
        this.userService.updateLoginUser(requestBody, loginUser);
    }

}
