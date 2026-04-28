package com.sanfran.community.infrastructure.entrypoints.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sanfran.community.domain.model.entity.Facility;
import com.sanfran.community.domain.usecase.FacilityUseCase;
import com.sanfran.community.infrastructure.entrypoints.api.dto.FacilityRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
                .andExpect(jsonPath("$.status").value(201));
    }

    @Test
    void createShouldReturn400WhenValidationFails() throws Exception {
        FacilityRequest request = new FacilityRequest("S", "Descripcion", "https://example.com/salon.jpg", 100);

        mockMvc.perform(post("/api/v1/facilities")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }
}