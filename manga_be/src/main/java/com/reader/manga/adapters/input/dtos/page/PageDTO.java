package com.reader.manga.adapters.input.dtos.page;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PageDTO(
   @NotBlank @NotNull String page,
   @NotNull Long chapter_id,
   @NotNull Long idChapter
) {}
