package com.reader.manga.dto.chapter;

public record ChapterDTO(
        String title,
        String description,
        Integer numberPages,
        Long manga
) {}
