package com.sanfran.community.infrastructure.entrypoints.api;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Standard API response envelope")
public record ApiResponse<T>(
    @Schema(description = "Response timestamp in ISO-8601 format", example = "2026-04-28T10:45:00Z")
        OffsetDateTime timestamp,
    @Schema(description = "HTTP status code returned by the API", example = "200")
        int status,
    @Schema(description = "Payload returned by the API")
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
