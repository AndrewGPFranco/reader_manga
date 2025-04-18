package com.reader.manga.adapters.input.dtos.animes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record AnimeDTO(
        MultipartFile file,
        @NotNull @NotBlank String id,
        @NotNull @NotBlank String title
) {}