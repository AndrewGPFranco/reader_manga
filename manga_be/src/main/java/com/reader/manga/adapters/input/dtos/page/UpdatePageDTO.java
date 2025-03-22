package com.reader.manga.adapters.input.dtos.page;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdatePageDTO(@NotNull @NotBlank String page) {}
