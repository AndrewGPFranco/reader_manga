package com.reader.manga.domain.valueobjects.users;

import com.reader.manga.domain.enums.RoleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UsersManagementVO(
        @NotBlank String username,
        @NotNull RoleType role,
        @NotNull LocalDate dataIn
) {}
