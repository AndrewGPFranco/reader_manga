package com.reader.manga.adapters.input.dtos.anime;

import lombok.Builder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Builder
public record AnimeDTO(
        @NotNull @NotBlank String title,
        @NotNull @NotBlank String uriImage
) {}