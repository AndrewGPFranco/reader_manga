package com.reader.manga.dto.manga;

import com.reader.manga.enums.StatusType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record MangaDTO(
        @NotNull @NotBlank String title,
        @NotNull @NotBlank String description,
        @NotNull Integer size,
        @NotNull Date creationDate,
        Date closingDate,
        @NotNull StatusType status,
        @NotNull @NotBlank String author,
        @NotNull @NotBlank String gender,
        @NotNull @NotBlank String image
) {}