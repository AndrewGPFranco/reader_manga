package com.reader.manga.dto;

import com.reader.manga.enums.StatusType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MangaDTO(
        @NotNull @NotBlank String title,
        @NotNull @NotBlank String description,
        @NotNull Integer size,
        @NotNull @NotBlank String creationDate,
        String closingDate,
        @NotNull StatusType status,
        @NotNull @NotBlank String author,
        @NotNull @NotBlank String gender,
        @NotNull @NotBlank String image
) {}
