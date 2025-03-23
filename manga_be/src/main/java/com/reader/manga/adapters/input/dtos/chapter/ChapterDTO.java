package com.reader.manga.adapters.input.dtos.chapter;

import com.reader.manga.domain.enums.StatusType;
import lombok.Builder;

@Builder
public record ChapterDTO(
        String title,
        Long mangaId
) {}