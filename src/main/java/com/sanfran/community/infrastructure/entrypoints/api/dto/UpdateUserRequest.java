package com.sanfran.community.infrastructure.entrypoints.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(
        @Schema(description = "Full name of the user", example = "Ana Maria Gomez")
        @NotBlank @Size(min = 2, max = 100) String names,
        @Schema(description = "Government identification document", example = "CC12345")
        @NotBlank @Size(min = 5, max = 20) String idDocument,
        @Schema(description = "Unique email address", example = "ana@test.com")
        @NotBlank @Email String email,
        @Schema(description = "Plain password (optional - leave null to keep current password)", example = "Password123")
        @Size(min = 8, max = 100) String password,
        @Schema(description = "Main role of the user", example = "ADMIN")
        @NotBlank String role,
        @Schema(description = "Secondary role or sub-role", example = "OWNER")
        @NotBlank String subRole
) {
}
