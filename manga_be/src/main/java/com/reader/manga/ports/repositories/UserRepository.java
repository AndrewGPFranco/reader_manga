package com.reader.manga.ports.repositories;

import com.reader.manga.domain.entities.users.User;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    @NotNull Page<User> findAll(@NotNull Pageable pageable);

}