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

    FAILED_TO_UPLOAD_FILE_TO_GCS(1001, "exception.internal_server_error.failed_to_upload_file_to_gcs"),

    /**
     * Not Found: 1100~1199
     */
    NOT_FOUND_API(1100, "exception.not_found.api"),

    NOT_FOUND_USER(1101, "exception.not_found.user"),

    NOT_FOUND_GIFT(1102, "exception.not_found.gift"),

    NOT_FOUND_USER_GIFT(1103, "exception.not_found.user_gift"),

    /**
     * Conflict: 1200~1299
     */
    CONFLICT_EMAIL(1200, "exception.conflict.email"),

    GIFT_ALREADY_OBTAINED(1201, "exception.conflict.gift_already_obtained"),

    /**
     * Forbidden: 1300~1399
     */
    USER_HAS_NO_PERMISSION(1300, "exception.forbidden.user_has_no_permission"),

    /**
     * Bad Request: 1400~1499
     */
    VALIDATION_ERROR(1400, "exception.bad_request.validation_error"),

    INVALID_REQUEST_PARAMETER(1401, "exception.bad_request.invalid_request_parameter"),

    INVALID_PASSWORD_SIZE(1402, "exception.bad_request.invalid_password_size"),

    TOO_SIMPLE_PASSWORD(1403, "exception.bad_request.too_simple_password"),

    USER_HAS_NO_GIFT(1404, "exception.bad_request.user_has_no_gift"),

    /**
     * Unauthorized: 1500~1599
     */
    USER_NOT_LOGGED_IN(1500, "exception.unauthorized.user_not_logged_in"),

    WRONG_PASSWORD(1501, "exception.unauthorized.wrong_password"),

    INVALID_ACCESS_TOKEN(1502, "exception.unauthorized.invalid_access_token"),

    EXPIRED_ACCESS_TOKEN(1503, "exception.unauthorized.expired_access_token");

    private final Integer code;

    private final String messageKey;

}
