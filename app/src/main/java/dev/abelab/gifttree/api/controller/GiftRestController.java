package dev.abelab.gifttree.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.*;
import lombok.*;
import dev.abelab.gifttree.annotation.Authenticated;
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
                @ApiResponse(code = 200, message = "成功"), //
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

}
