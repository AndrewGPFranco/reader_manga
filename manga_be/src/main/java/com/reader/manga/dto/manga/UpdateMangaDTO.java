package com.reader.manga.dto.manga;

import com.reader.manga.enums.StatusType;
import lombok.Builder;

import java.util.Date;

@Builder
public record UpdateMangaDTO(
        String title,
        String description,
        Integer size,
        Date creationDate,
        Date closingDate,
        StatusType status,
        String author,
        String gender,
        String image,
        boolean isFavorite
) {}