package com.sanfran.community.domain.usecase;

import com.sanfran.community.domain.model.entity.User;

import java.util.List;
import java.util.UUID;

public interface IUserUseCase {
    User create(User user);

    User update(UUID id, User user);

    void delete(UUID id);

    User findById(UUID id);

    List<User> findAll();

    List<User> findByNames(String names);

    List<User> findByEmail(String email);
}
