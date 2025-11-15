package com.reader.manga.domain.valueobjects.mangas;

import com.reader.manga.domain.enums.StatusType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

public record HistoryMangaOutputVO(
        @NotNull UUID id,
        @NotBlank String titleChapter,
        @NotBlank String titleManga,
        @NotNull StatusType statusType,
        @NotNull OffsetDateTime lastCheck
) {
}