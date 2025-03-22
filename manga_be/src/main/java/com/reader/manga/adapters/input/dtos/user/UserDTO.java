package com.reader.manga.adapters.input.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record UserDTO(
        @NotNull @NotBlank String firstName,
        @NotNull @NotBlank String fullName,
        @NotNull @NotBlank String username,
        @NotNull @NotBlank String email,
        @NotNull @NotBlank String password,
        @NotNull LocalDate dateBirth
) {}