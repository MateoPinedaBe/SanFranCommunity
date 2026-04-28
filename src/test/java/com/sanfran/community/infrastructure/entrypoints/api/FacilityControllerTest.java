package com.sanfran.community.infrastructure.entrypoints.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanfran.community.domain.model.entity.Facility;
import com.sanfran.community.domain.model.exception.BusinessRuleException;
import com.sanfran.community.domain.usecase.FacilityUseCase;
import com.sanfran.community.infrastructure.entrypoints.api.dto.FacilityRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FacilityControllerTest {

    private MockMvc mockMvc;
    private FacilityUseCase useCase;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        useCase = mock(FacilityUseCase.class);
        objectMapper = new ObjectMapper().findAndRegisterModules();
        mockMvc = MockMvcBuilders.standaloneSetup(new FacilityController(useCase))
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createShouldReturn201() throws Exception {
        FacilityRequest request = new FacilityRequest("Salon Social", "Descripcion", "https://example.com/salon.jpg", 100);
        Facility facility = new Facility(UUID.randomUUID(), "Salon Social", "Descripcion", "https://example.com/salon.jpg", 100);
        when(useCase.create(any(Facility.class))).thenReturn(facility);

        mockMvc.perform(post("/api/v1/facilities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
            .andExpect(jsonPath("$.status").value(201))
            .andExpect(jsonPath("$.data.name").value("Salon Social"))
            .andExpect(jsonPath("$.data.capacity").value(100));
    }

    @Test
    void createShouldReturn400WhenValidationFails() throws Exception {
        FacilityRequest request = new FacilityRequest("S", "Descripcion", "https://example.com/salon.jpg", 100);

        mockMvc.perform(post("/api/v1/facilities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.data.error").value("Validation Error"))
            .andExpect(jsonPath("$.data.field").value("name"));
    }

    @Test
    void findByIdShouldAcceptQueryParam() throws Exception {
        UUID id = UUID.randomUUID();
        Facility facility = new Facility(id, "Salon Social", "Descripcion", "https://example.com/salon.jpg", 100);
        when(useCase.findById(id)).thenReturn(facility);

        mockMvc.perform(get("/api/v1/facilities").param("id", id.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.name").value("Salon Social"));
    }

    @Test
    void updateShouldAcceptIdQueryParam() throws Exception {
        UUID id = UUID.randomUUID();
        FacilityRequest request = new FacilityRequest("Salon Social", "Descripcion", "https://example.com/salon.jpg", 100);
        Facility updated = new Facility(id, "Salon Social", "Descripcion", "https://example.com/salon.jpg", 100);

        when(useCase.update(eq(id), any(Facility.class))).thenReturn(updated);

        mockMvc.perform(put("/api/v1/facilities")
                        .param("id", id.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.id").value(id.toString()));
    }

    @Test
    void updateShouldAcceptNameQueryParam() throws Exception {
        UUID id = UUID.randomUUID();
        FacilityRequest request = new FacilityRequest("Salon Social", "Descripcion", "https://example.com/salon.jpg", 100);
        Facility existing = new Facility(id, "Salon Social", "Descripcion", "https://example.com/salon.jpg", 100);

        when(useCase.findByName("Salon Social")).thenReturn(java.util.List.of(existing));
        when(useCase.update(eq(id), any(Facility.class))).thenReturn(existing);

        mockMvc.perform(put("/api/v1/facilities")
                        .param("name", "Salon Social")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data.id").value(id.toString()));
    }

    @Test
    void deleteShouldAcceptIdQueryParam() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/facilities").param("id", id.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").value("Facility deleted successfully."));

        verify(useCase).delete(id);
    }

    @Test
    void deleteShouldAcceptNameQueryParam() throws Exception {
        UUID id = UUID.randomUUID();
        Facility existing = new Facility(id, "Salon Social", "Descripcion", "https://example.com/salon.jpg", 100);

        when(useCase.findByName("Salon Social")).thenReturn(java.util.List.of(existing));

        mockMvc.perform(delete("/api/v1/facilities").param("name", "Salon Social"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(200))
                .andExpect(jsonPath("$.data").value("Facility deleted successfully."));

        verify(useCase).delete(id);
    }

    @Test
    void deleteShouldReturn409WhenFacilityHasReservations() throws Exception {
        UUID id = UUID.randomUUID();
        doThrow(new DataIntegrityViolationException("Constraint violation")).when(useCase).delete(id);

        mockMvc.perform(delete("/api/v1/facilities/{id}", id))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.data.error").value("Data Integrity Violation"));
    }

    @Test
    void updateByNameShouldReturn422WhenNameIsAmbiguous() throws Exception {
        FacilityRequest request = new FacilityRequest("Salon Social", "Descripcion", "https://example.com/salon.jpg", 100);
        when(useCase.findByName("Salon Social")).thenReturn(java.util.List.of(
                new Facility(UUID.randomUUID(), "Salon Social", "A", "https://example.com/a.jpg", 10),
                new Facility(UUID.randomUUID(), "Salon Social", "B", "https://example.com/b.jpg", 20)
        ));

        mockMvc.perform(put("/api/v1/facilities")
                        .param("name", "Salon Social")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.status").value(422))
                .andExpect(jsonPath("$.data.error").value("Business Rule Violation"));
    }
}