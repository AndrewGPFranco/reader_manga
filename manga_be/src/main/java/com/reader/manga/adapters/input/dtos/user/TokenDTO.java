package com.reader.manga.adapters.input.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TokenDTO(
    @NotNull @NotBlank String token
) {}