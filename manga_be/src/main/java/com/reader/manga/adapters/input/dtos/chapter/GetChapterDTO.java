package com.reader.manga.adapters.input.dtos.chapter;

import com.reader.manga.domain.enums.StatusType;

public record GetChapterDTO(
        Long id,
        String title,
        Integer numberPages,
        StatusType status,
        Integer readingProgress,
        String urlImageManga,
        String nameManga,
        int numberPageOfPageable
) {}