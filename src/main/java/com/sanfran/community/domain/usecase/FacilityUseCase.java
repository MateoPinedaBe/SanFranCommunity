package com.sanfran.community.domain.usecase;

import com.sanfran.community.domain.model.entity.Facility;
import com.sanfran.community.domain.model.exception.NotFoundException;
import com.sanfran.community.domain.model.exception.ValidationException;
import com.sanfran.community.domain.model.vo.DomainValidators;
import com.sanfran.community.domain.usecase.port.FacilityRepository;
import com.sanfran.community.domain.usecase.port.ReservationRepository;
import com.sanfran.community.domain.model.exception.BusinessRuleException;

import java.util.List;
import java.util.UUID;

public class FacilityUseCase {

    private final FacilityRepository repository;
    private final ReservationRepository reservationRepository;

    public FacilityUseCase(FacilityRepository repository, ReservationRepository reservationRepository) {
        this.repository = repository;
        this.reservationRepository = reservationRepository;
    }

    public Facility create(Facility facility) {
        DomainValidators.validateFacility(facility);
        return repository.save(facility);
    }

    public Facility update(UUID id, Facility facility) {
        if (id == null) {
            throw new ValidationException("id", "ID must not be null.");
        }

        DomainValidators.validateFacility(facility);
        repository.findById(id)
                .orElseThrow(() -> new NotFoundException("The Facility was not found in the database."));

        return repository.save(new Facility(id, facility.name(), facility.description(), facility.imageUrl(), facility.capacity()));
    }

    public void delete(UUID id) {
        if (id == null) {
            throw new ValidationException("id", "ID must not be null.");
        }
        if (!repository.existsById(id)) {
            throw new NotFoundException("The Facility was not found in the database.");
        }
        long active = reservationRepository.countActiveByFacilityId(id);
        if (active > 0) {
            throw new BusinessRuleException("No se puede eliminar la instalación porque tiene reservas activas.");
        }

        // Remove cancelled reservations referencing this facility to avoid FK constraint
        reservationRepository.deleteCancelledByFacilityId(id);
        repository.deleteById(id);
    }

    public Facility findById(UUID id) {
        if (id == null) {
            throw new ValidationException("id", "ID must not be null.");
        }
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("The Facility was not found in the database."));
    }

    public List<Facility> findAll() {
        return repository.findAll();
    }

    public List<Facility> findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new ValidationException("name", "Name must not be blank.");
        }
        return repository.findByNameContainingIgnoreCase(name);
    }
}
