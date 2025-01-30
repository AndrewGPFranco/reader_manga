package com.reader.manga.dto.chapter;

import lombok.Builder;

@Builder
public record ChapterDTO(
        String title,
        String description,
        Long mangaId
) {}
