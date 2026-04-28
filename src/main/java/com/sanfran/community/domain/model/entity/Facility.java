package com.sanfran.community.domain.model.entity;

import java.util.UUID;

public record Facility(
        UUID id,
        String name,
        String description,
        String imageUrl,
        Integer capacity
) {
}
