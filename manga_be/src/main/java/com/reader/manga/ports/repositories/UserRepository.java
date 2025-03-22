package com.reader.manga.ports.repositories;

import com.reader.manga.domain.entities.users.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

}