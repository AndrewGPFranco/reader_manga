package com.reader.manga.dto;

import com.reader.manga.enums.StatusType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UserMangaDTO(
        @NotNull Long idManga,
        @NotNull Long idUser,
        @NotNull LocalDate signatureDate,
        @NotNull StatusType status,
        Integer nota,
        String comment
) {}