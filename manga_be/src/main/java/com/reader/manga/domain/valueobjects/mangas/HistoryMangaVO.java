package com.reader.manga.domain.valueobjects.mangas;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record HistoryMangaVO(
    @NotNull Long idChapter,
    @NotNull Integer currentProgress
) {
}
