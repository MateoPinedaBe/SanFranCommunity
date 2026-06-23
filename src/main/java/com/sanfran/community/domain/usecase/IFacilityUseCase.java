package com.sanfran.community.domain.usecase;

import com.sanfran.community.domain.model.entity.Facility;

import java.util.List;
import java.util.UUID;

public interface IFacilityUseCase {
    Facility create(Facility facility);

    Facility update(UUID id, Facility facility);

    void delete(UUID id);

    Facility findById(UUID id);

    List<Facility> findAll();

    List<Facility> findByName(String name);
}
