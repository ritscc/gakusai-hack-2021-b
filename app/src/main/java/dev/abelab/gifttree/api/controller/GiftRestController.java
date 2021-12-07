package dev.abelab.gifttree.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.*;
import lombok.*;
import dev.abelab.gifttree.annotation.Authenticated;
import dev.abelab.gifttree.api.request.GiftShareRequest;
import dev.abelab.gifttree.api.response.GiftsResponse;
import dev.abelab.gifttree.db.entity.User;
import dev.abelab.gifttree.service.GiftService;

@Api(tags = "Gift")
@RestController
@RequestMapping(path = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
@Authenticated
public class GiftRestController {

    private final GiftService giftService;

    /**
     * ギフト受け取りAPI
     *
     * @param giftId    ギフトID
     * @param loginUser ログインユーザ
     */
    @ApiOperation( //
        value = "ギフトの受け取り", //
        notes = "ギフトを受け取る。" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 200, message = "受け取り成功"), //
                @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
                @ApiResponse(code = 409, message = "既に受け取り済み"), //
        } //
    )
    @PostMapping(value = "/gifts/{gift_id}/obtain")
    @ResponseStatus(HttpStatus.CREATED)
    public void obtainGift( //
        @ApiParam(name = "gift_id", required = true, value = "ギフトID") @PathVariable("gift_id") final int giftId, //
        @ModelAttribute("LoginUser") final User loginUser //
    ) {
        this.giftService.obtainGift(giftId, loginUser);
    }

    /**
     * 所持ギフト一覧取得API
     *
     * @param loginUser ログインユーザ
     *
     * @return ギフト一覧
     */
    @ApiOperation( //
        value = "所持ギフト一覧の取得", //
        notes = "所持ギフト一覧を取得する。" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 200, message = "取得成功"), //
                @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
        } //
    )
    @GetMapping(value = "/users/me/gifts")
    @ResponseStatus(HttpStatus.OK)
    public GiftsResponse getLoginUserGifts( //
        @ModelAttribute("LoginUser") final User loginUser //
    ) {
        return this.giftService.getLoginUserGifts(loginUser);
    }

    /**
     * ギフトおすそわけAPI
     *
     * @param userId      ユーザID
     * @param requestBody ギフトおすそわけ情報
     * @param loginUser   ログインユーザ
     */
    @ApiOperation( //
        value = "ギフトの受け取り", //
        notes = "ギフトを受け取る。" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 200, message = "受け取り成功"), //
                @ApiResponse(code = 400, message = "ギフトを所持していない"), //
                @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
                @ApiResponse(code = 404, message = "ギフト/ユーザが存在しない"), //
        } //
    )
    @PostMapping(value = "/users/{user_id}/gifts")
    @ResponseStatus(HttpStatus.OK)
    public void shareGift( //
        @ApiParam(name = "user_id", required = true, value = "ユーザID") @PathVariable("user_id") final int userId, //
        @Validated @ApiParam(name = "body", required = true, value = "ギフトおすそわけ情報") @RequestBody final GiftShareRequest requestBody, //
        @ModelAttribute("LoginUser") final User loginUser //
    ) {
        this.giftService.shareGift(userId, requestBody, loginUser);
    }

}
