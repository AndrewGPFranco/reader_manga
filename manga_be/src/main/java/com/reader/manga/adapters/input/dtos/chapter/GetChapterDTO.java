package com.reader.manga.adapters.input.dtos.chapter;

public record GetChapterDTO(
        String title,
        String description,
        Integer numberPages
) {}
