package dev.abelab.gifttree.exception;

import lombok.*;

/**
 * エラーコード
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * Internal Server Error: 1000~1099
     */
    UNEXPECTED_ERROR(1000, "exception.internal_server_error.unexpected_error"),

    /**
     * Not Found: 1100~1199
     */
    NOT_FOUND_API(1100, "exception.not_found.api"),

    /**
     * Conflict: 1200~1299
     */

    /**
     * Forbidden: 1300~1399
     */
    USER_HAS_NO_PERMISSION(1300, "exception.forbidden.user_has_no_permission"),

    /**
     * Bad Request: 1400~1499
     */
    VALIDATION_ERROR(1400, "exception.bad_request.validation_error"),

    INVALID_REQUEST_PARAMETER(1401, "exception.bad_request.invalid_request_parameter"),

    /**
     * Unauthorized: 1500~1599
     */
    USER_NOT_LOGGED_IN(1500, "exception.unauthorized.user_not_logged_in");

    private final Integer code;

    private final String messageKey;

}
