package com.reader.manga.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record RecoverUserDTO(
        @NotNull @NotBlank String firstName,
        @NotNull @NotBlank String fullName,
        @NotNull @NotBlank String username,
        @NotNull @NotBlank String email,
        @NotNull LocalDate dateBirth
) {}