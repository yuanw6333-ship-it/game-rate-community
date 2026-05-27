package com.gamerate.common.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

    private Integer code;

    private String message;

    private T data;

    private LocalDateTime timestamp;

    public static <T> Result<T> success() {
        return success(null);
    }

    public static <T> Result<T> success(T data) {
        return of(ResultCode.SUCCESS.getCode(), ResultCode.SUCCESS.getMessage(), data);
    }

    public static <T> Result<T> failed(String message) {
        return of(ResultCode.INTERNAL_ERROR.getCode(), message, null);
    }

    public static <T> Result<T> failed(ResultCode resultCode) {
        return of(resultCode.getCode(), resultCode.getMessage(), null);
    }

    public static <T> Result<T> failed(ResultCode resultCode, String message) {
        return of(resultCode.getCode(), message, null);
    }

    public static <T> Result<T> failed(Integer code, String message) {
        return of(code, message, null);
    }

    public static <T> Result<T> of(Integer code, String message, T data) {
        return new Result<>(code, message, data, LocalDateTime.now());
    }
}
