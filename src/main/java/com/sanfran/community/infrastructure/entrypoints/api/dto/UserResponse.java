package com.sanfran.community.infrastructure.entrypoints.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record UserResponse(
        @Schema(description = "Unique user identifier")
        UUID id,
        @Schema(description = "Full name of the user")
        String names,
        @Schema(description = "Government identification document")
        String idDocument,
        @Schema(description = "Unique email address")
        String email,
        @Schema(description = "Main role of the user")
        String role,
        @Schema(description = "Secondary role or sub-role")
        String subRole
) {
}
