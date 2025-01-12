package com.reader.manga.dto.user;

import com.reader.manga.model.Role;

import java.util.List;

public record RecoveryUserDto(
        Long id,
        String email,
        List<Role> roles
) {}
