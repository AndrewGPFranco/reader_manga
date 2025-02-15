package com.reader.manga.vo;

import com.reader.manga.enums.StatusType;
import com.reader.manga.model.Chapter;
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
        String title,
        String author,
        Integer size,
        String gender,
        StatusType status,
        Date creationDate,
        Date endDate,
        String description,
        List<Chapter> chapters
) {}
