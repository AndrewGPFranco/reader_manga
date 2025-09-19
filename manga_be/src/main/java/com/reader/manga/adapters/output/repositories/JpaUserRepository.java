package com.reader.manga.adapters.output.repositories;

import com.reader.manga.domain.entities.users.User;
import com.reader.manga.ports.repositories.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JpaUserRepository extends UserRepository, JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

}