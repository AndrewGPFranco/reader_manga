package com.reader.manga.dto.manga;

import jakarta.validation.constraints.NotNull;

public record UpdateFavoriteDTO(
        @NotNull boolean isFavorite
) {}
