package com.reader.manga.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginResponseDTO(
        @NotNull @NotBlank String email,
        @NotNull @NotBlank String password,
        @NotNull @NotBlank String token
) {}
