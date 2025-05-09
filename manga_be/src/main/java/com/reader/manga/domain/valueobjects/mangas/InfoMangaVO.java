package com.reader.manga.domain.valueobjects.mangas;

import com.reader.manga.domain.enums.StatusType;
import com.reader.manga.domain.entities.mangas.Chapter;
import lombok.Builder;

import java.util.Date;
import java.util.List;

/**
 * VO usado para enviar informações sobre um mangá.
 * @param title
 * @param author
 * @param size
 * @param gender
 * @param status
 * @param creationDate
 * @param endDate
 * @param description
 * @param chapters
 */
@Builder
public record InfoMangaVO(
        Long id,
        String title,
        String image,
        Long nota,
        Integer size,
        String author,
        String gender,
        Date endDate,
        StatusType status,
        Date creationDate,
        String description,
        List<Chapter> chapters,
        boolean isInUserLibrary
) {}
