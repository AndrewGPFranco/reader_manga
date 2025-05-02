package com.reader.manga.adapters.input.dtos.chapter;

import lombok.Builder;

@Builder
public record ChapterDTO(
        String title,
        Long mangaId
) {}