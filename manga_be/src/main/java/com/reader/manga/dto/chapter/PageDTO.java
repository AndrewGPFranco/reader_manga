package com.reader.manga.dto.chapter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PageDTO(
   @NotBlank @NotNull String page,
   @NotNull Long chapter_id
) {}
