package com.reader.manga.dto.chapter;

public record GetChapterDTO(
        String title,
        String description,
        Integer numberPages
) {}
