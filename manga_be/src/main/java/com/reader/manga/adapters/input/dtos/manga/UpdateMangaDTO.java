package com.reader.manga.adapters.input.dtos.manga;

import com.reader.manga.domain.enums.StatusType;
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