package com.reader.manga.dto;

import com.reader.manga.model.Manga;

public record ChapterDTO(
        String title,
        String description,
        Integer numberPages,
        Long manga
) {}
