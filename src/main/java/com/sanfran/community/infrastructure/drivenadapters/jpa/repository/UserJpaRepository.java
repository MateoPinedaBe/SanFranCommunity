package com.sanfran.community.infrastructure.drivenadapters.jpa.repository;

import com.sanfran.community.infrastructure.drivenadapters.jpa.entity.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserJpaRepository extends JpaRepository<UserJpaEntity, UUID> {
    List<UserJpaEntity> findByNamesContainingIgnoreCase(String names);

    java.util.Optional<UserJpaEntity> findByEmailIgnoreCase(String email);

    java.util.Optional<UserJpaEntity> findByIdDocumentIgnoreCase(String idDocument);
}
