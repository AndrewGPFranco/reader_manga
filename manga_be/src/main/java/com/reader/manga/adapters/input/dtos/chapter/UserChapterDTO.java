package com.reader.manga.adapters.input.dtos.chapter;

import jakarta.validation.constraints.NotNull;

public record UserChapterDTO(
   @NotNull Long idUser,
   @NotNull Long idChapter,
   @NotNull Integer progress
) {}
