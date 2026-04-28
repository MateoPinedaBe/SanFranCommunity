package com.sanfran.community.domain.usecase.port;

import com.sanfran.community.domain.model.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    User save(User user);

    Optional<User> findById(UUID id);

    boolean existsById(UUID id);

    void deleteById(UUID id);

    List<User> findAll();

    List<User> findByNamesContainingIgnoreCase(String names);

    Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByIdDocumentIgnoreCase(String idDocument);
}
