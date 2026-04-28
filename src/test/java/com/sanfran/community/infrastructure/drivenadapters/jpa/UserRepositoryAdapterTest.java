package com.sanfran.community.infrastructure.drivenadapters.jpa;

import com.sanfran.community.domain.model.entity.User;
import com.sanfran.community.infrastructure.drivenadapters.jpa.entity.UserJpaEntity;
import com.sanfran.community.infrastructure.drivenadapters.jpa.repository.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class UserRepositoryAdapterTest {

    @Mock
    private UserJpaRepository jpaRepository;

    private UserRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adapter = new UserRepositoryAdapter(jpaRepository);
    }

    @Test
    void findByIdShouldMapCorrectly() {
        UUID id = UUID.randomUUID();
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(id);
        entity.setNames("Ana Maria");
        entity.setIdDocument("CC12345");
        entity.setEmail("ana@test.com");
        entity.setPassword("Password123");
        entity.setRole("ADMIN");
        entity.setSubRole("OWNER");

        when(jpaRepository.findById(id)).thenReturn(Optional.of(entity));

        Optional<User> result = adapter.findById(id);

        assertThat(result).isPresent();
        assertThat(result.get().email()).isEqualTo("ana@test.com");
    }

    @Test
    void findAllShouldReturnMappedList() {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(UUID.randomUUID());
        entity.setNames("Ana Maria");
        entity.setIdDocument("CC12345");
        entity.setEmail("ana@test.com");
        entity.setPassword("Password123");
        entity.setRole("ADMIN");
        entity.setSubRole("OWNER");

        when(jpaRepository.findAll()).thenReturn(List.of(entity));

        List<User> result = adapter.findAll();

        assertThat(result).hasSize(1);
        assertThat(result.getFirst().names()).isEqualTo("Ana Maria");
    }
}