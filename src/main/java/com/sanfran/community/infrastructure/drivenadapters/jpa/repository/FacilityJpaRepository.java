package com.sanfran.community.infrastructure.drivenadapters.jpa.repository;

import com.sanfran.community.infrastructure.drivenadapters.jpa.entity.FacilityJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FacilityJpaRepository extends JpaRepository<FacilityJpaEntity, UUID> {
    List<FacilityJpaEntity> findByNameContainingIgnoreCase(String name);
}
