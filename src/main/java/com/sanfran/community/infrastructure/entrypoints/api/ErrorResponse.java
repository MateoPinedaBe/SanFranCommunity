package com.sanfran.community.infrastructure.entrypoints.api;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Error payload returned when the request cannot be processed")
public record ErrorResponse(
        @Schema(description = "High-level error category", example = "Validation Error")
        String error,
        @Schema(description = "Request field related to the error when applicable", example = "email", nullable = true)
        String field,
        @Schema(description = "Human-readable error detail", example = "Email must be valid.")
        String message
) {
}