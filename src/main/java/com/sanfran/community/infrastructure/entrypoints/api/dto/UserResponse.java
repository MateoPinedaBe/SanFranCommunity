package com.sanfran.community.infrastructure.entrypoints.api.dto;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String names,
        String idDocument,
        String email,
        String role,
        String subRole
) {
}
