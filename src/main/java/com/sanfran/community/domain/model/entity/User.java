package com.sanfran.community.domain.model.entity;

import java.util.UUID;

public record User(
        UUID id,
        String names,
        String idDocument,
        String email,
        String password,
        String role,
        String subRole
) {
}
