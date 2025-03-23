package com.reader.manga.adapters.input.dtos.chapter;

import com.reader.manga.domain.enums.StatusType;

public record UpdateChapterDTO(
        String title,
        StatusType status,
        Integer readingProgress
) {}