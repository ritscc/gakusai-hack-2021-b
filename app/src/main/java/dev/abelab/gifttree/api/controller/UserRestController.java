package dev.abelab.gifttree.api.controller;

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

@Api(tags = "User")
@RestController
@RequestMapping(path = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
@Authenticated
public class UserRestController {

    private final UserService userService;

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

}
