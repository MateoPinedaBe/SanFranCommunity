package com.sanfran.community.infrastructure.drivenadapters.jpa;

import com.sanfran.community.domain.model.entity.User;
import com.sanfran.community.domain.usecase.port.UserRepository;
import com.sanfran.community.infrastructure.drivenadapters.jpa.entity.UserJpaEntity;
import com.sanfran.community.infrastructure.drivenadapters.jpa.repository.UserJpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository repository;

    public UserRepositoryAdapter(UserJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public User save(User user) {
        UserJpaEntity entity = toEntity(user);
        UserJpaEntity saved = repository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<User> findById(UUID id) {
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
    public List<User> findAll() {
        return repository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public List<User> findByNamesContainingIgnoreCase(String names) {
        return repository.findByNamesContainingIgnoreCase(names).stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<User> findByEmailIgnoreCase(String email) {
        return repository.findByEmailIgnoreCase(email).map(this::toDomain);
    }

    @Override
    public Optional<User> findByIdDocumentIgnoreCase(String idDocument) {
        return repository.findByIdDocumentIgnoreCase(idDocument).map(this::toDomain);
    }

    private UserJpaEntity toEntity(User user) {
        UserJpaEntity entity = new UserJpaEntity();
        entity.setId(user.id());
        entity.setNames(user.names());
        entity.setIdDocument(user.idDocument());
        entity.setEmail(user.email());
        entity.setPassword(user.password());
        entity.setRole(user.role());
        entity.setSubRole(user.subRole());
        return entity;
    }

    private User toDomain(UserJpaEntity entity) {
        return new User(
                entity.getId(),
                entity.getNames(),
                entity.getIdDocument(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getRole(),
                entity.getSubRole()
        );
    }
}
