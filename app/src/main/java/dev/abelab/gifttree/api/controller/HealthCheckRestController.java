package dev.abelab.gifttree.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import io.swagger.annotations.*;
import lombok.*;

@Api(tags = "Health Check")
@RestController
@RequestMapping(path = "/api/health", produces = MediaType.APPLICATION_JSON_VALUE)
@Validated
@RequiredArgsConstructor
public class HealthCheckRestController {

    /**
     * ヘルスチェックAPI
     */
    @ApiOperation( //
        value = "ヘルスチェック", //
        notes = "APIのヘルスチェックを行う。" //
    )
    @ApiResponses( //
        value = { //
                @ApiResponse(code = 200, message = "成功"), //
        })
    @GetMapping(value = "")
    @ResponseStatus(HttpStatus.OK)
    public void healthCheck() {
        return;
    }

}
