package com.reader.manga.dto.chapter;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdatePageDTO(@NotNull @NotBlank String page) {}
