package com.reader.manga.dto.user;

import com.reader.manga.enums.RoleNameType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserDto(
        @NotNull @NotBlank String email,
        @NotNull @NotBlank String password,
        RoleNameType role
) {}
