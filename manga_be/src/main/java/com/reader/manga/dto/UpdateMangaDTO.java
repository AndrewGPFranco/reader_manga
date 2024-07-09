package com.reader.manga.dto;

import com.reader.manga.enums.StatusType;

public record UpdateMangaDTO(
        String title,
        String description,
        Integer size,
        String creationDate,
        String closingDate,
        StatusType status,
        String author,
        String gender,
        String image
) {}