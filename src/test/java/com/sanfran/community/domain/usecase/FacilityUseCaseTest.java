package com.sanfran.community.domain.usecase;

import com.sanfran.community.domain.model.entity.Facility;
import com.sanfran.community.domain.model.exception.BusinessRuleException;
import com.sanfran.community.domain.model.exception.NotFoundException;
import com.sanfran.community.domain.usecase.port.FacilityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

class FacilityUseCaseTest {

    @Mock
    private FacilityRepository repository;

    private FacilityUseCase useCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        useCase = new FacilityUseCase(repository);
    }

    @Test
    void createShouldPersistValidFacility() {
        Facility input = new Facility(null, "Salon Social", "Descripcion", "https://example.com/salon.jpg", 100);
        Facility saved = new Facility(UUID.randomUUID(), "Salon Social", "Descripcion", "https://example.com/salon.jpg", 100);

        when(repository.save(input)).thenReturn(saved);

        Facility result = useCase.create(input);

        assertThat(result.id()).isNotNull();
        assertThat(result.name()).isEqualTo("Salon Social");
    }

    @Test
    void createShouldRejectInvalidCapacity() {
        Facility input = new Facility(null, "Salon Social", "Descripcion", "https://example.com/salon.jpg", 0);

        assertThatThrownBy(() -> useCase.create(input))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("Capacity must be between 1 and 10000.");
    }

    @Test
    void deleteShouldThrowWhenFacilityDoesNotExist() {
        UUID id = UUID.randomUUID();
        when(repository.existsById(id)).thenReturn(false);

        assertThatThrownBy(() -> useCase.delete(id))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("The Facility was not found in the database.");
    }
}