package com.reader.manga.domain.valueobjects.animes;

import jakarta.validation.constraints.NotNull;

public record AvaliacaoAnimeVO(
        @NotNull Integer nota,
        @NotNull Long idAnime
) {}