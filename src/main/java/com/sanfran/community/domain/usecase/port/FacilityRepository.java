package com.sanfran.community.domain.usecase.port;

import com.sanfran.community.domain.model.entity.Facility;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FacilityRepository {
    Facility save(Facility facility);

    Optional<Facility> findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);

    List<Facility> findAll();

    List<Facility> findByNameContainingIgnoreCase(String name);
}
