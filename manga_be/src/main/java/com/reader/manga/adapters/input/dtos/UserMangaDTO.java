package com.reader.manga.adapters.input.dtos;

import com.reader.manga.domain.enums.StatusType;
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