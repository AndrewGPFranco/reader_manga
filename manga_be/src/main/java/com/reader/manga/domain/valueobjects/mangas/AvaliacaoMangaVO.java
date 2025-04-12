package com.reader.manga.domain.valueobjects.mangas;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record AvaliacaoMangaVO(
    @NotNull Integer nota,
    @NotNull Long idManga
) {}