package com.reader.manga.dto;

import com.reader.manga.enums.StatusType;

public record UpdateChapterDTO(
        String title,
        String description,
        Integer numberPages
) {}
