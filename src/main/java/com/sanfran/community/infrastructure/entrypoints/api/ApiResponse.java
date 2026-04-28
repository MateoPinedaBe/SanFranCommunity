package com.sanfran.community.infrastructure.entrypoints.api;

import java.time.OffsetDateTime;

public record ApiResponse<T>(
        OffsetDateTime timestamp,
        int status,
        T data
) {
    public static <T> ApiResponse<T> success(T data) {
        return success(200, data);
    }

    public static <T> ApiResponse<T> success(int status, T data) {
        return new ApiResponse<>(OffsetDateTime.now(), status, data);
    }

    public static <T> ApiResponse<T> error(int status, T data) {
        return new ApiResponse<>(OffsetDateTime.now(), status, data);
    }
}
