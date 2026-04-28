package com.sanfran.community.infrastructure.drivenadapters.jpa;

import com.sanfran.community.domain.model.entity.Facility;
import com.sanfran.community.domain.usecase.port.FacilityRepository;
import com.sanfran.community.infrastructure.drivenadapters.jpa.entity.FacilityJpaEntity;
import com.sanfran.community.infrastructure.drivenadapters.jpa.repository.FacilityJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FacilityRepositoryAdapter implements FacilityRepository {

    private final FacilityJpaRepository repository;

    public FacilityRepositoryAdapter(FacilityJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Facility save(Facility facility) {
        FacilityJpaEntity entity = toEntity(facility);
        FacilityJpaEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Facility> findById(UUID id) {
        return repository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public List<Facility> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<Facility> findByNameContainingIgnoreCase(String name) {
        return repository.findByNameContainingIgnoreCase(name).stream().map(this::toDomain).toList();
    }

    private FacilityJpaEntity toEntity(Facility facility) {
        FacilityJpaEntity entity = new FacilityJpaEntity();
        entity.setId(facility.id());
        entity.setName(facility.name());
        entity.setDescription(facility.description());
        entity.setImageUrl(facility.imageUrl());
        entity.setCapacity(facility.capacity());
        return entity;
    }

    private Facility toDomain(FacilityJpaEntity entity) {
        return new Facility(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getImageUrl(),
                entity.getCapacity()
        );
    }
}
