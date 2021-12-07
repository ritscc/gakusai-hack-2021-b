package dev.abelab.gifttree.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.*;
import lombok.*;
import dev.abelab.gifttree.annotation.Authenticated;
import dev.abelab.gifttree.api.response.RankingResponse;
import dev.abelab.gifttree.db.entity.User;
import dev.abelab.gifttree.service.RankingService;

@Api(tags = "Ranking")
@RestController
@RequestMapping(path = "/api/ranking", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
@Authenticated
public class RankingRestController {

    private final RankingService rankingService;

    /**
     * ランキング取得API
     *
     * @param loginUser ログインユーザ
     *
     * @return ランキングレスポンス
     */
    @ApiOperation( //
        value = "ランキングの取得", //
        notes = "ランキングを取得する。" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 200, message = "取得成功"), //
                @ApiResponse(code = 401, message = "ユーザがログインしていない"), //
        } //
    )
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public RankingResponse getRanking( //
        @ModelAttribute("LoginUser") final User loginUser //
    ) {
        return this.rankingService.getRanking(loginUser);
    }

}
