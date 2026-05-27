package com.gamerate.common.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCode {

    SUCCESS(200, "success"),
    BAD_REQUEST(400, "Bad request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not found"),
    VALIDATION_ERROR(422, "Validation failed"),
    BUSINESS_ERROR(1001, "Business error"),
    INTERNAL_ERROR(500, "Internal server error");

    private final Integer code;
    private final String message;
}
