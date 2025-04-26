package com.reader.manga.adapters.input.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AnimeUserDTO(
    @NotNull Long idUser,
    @NotNull Long idAnime
) {}