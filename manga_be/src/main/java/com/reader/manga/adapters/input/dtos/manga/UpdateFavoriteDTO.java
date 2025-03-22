package com.reader.manga.adapters.input.dtos.manga;

import jakarta.validation.constraints.NotNull;

public record UpdateFavoriteDTO(
        @NotNull boolean isFavorite
) {}
