package com.sanfran.community.infrastructure.entrypoints.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserRequest(
        @NotBlank @Size(min = 2, max = 100) String names,
        @NotBlank @Size(min = 5, max = 20) String idDocument,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8, max = 100) String password,
        @NotBlank String role,
        @NotBlank String subRole
) {
}
