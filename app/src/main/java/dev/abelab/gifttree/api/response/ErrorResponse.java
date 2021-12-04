package dev.abelab.gifttree.api.response;

import lombok.*;

/**
 * エラーレスポンス
 */
@Value
@Builder
@AllArgsConstructor
public class ErrorResponse {

    /**
     * エラーメッセージ
     */
    String message;

    /**
     * エラーコード
     */
    Integer code;

}
