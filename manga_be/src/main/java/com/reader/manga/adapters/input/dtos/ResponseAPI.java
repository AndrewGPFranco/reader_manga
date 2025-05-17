package com.reader.manga.adapters.input.dtos;

import lombok.Builder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Builder
public record ResponseAPI(
        @NotNull @NotBlank String message,
        @NotNull Integer statusCode,
        Object responseObject
) {}