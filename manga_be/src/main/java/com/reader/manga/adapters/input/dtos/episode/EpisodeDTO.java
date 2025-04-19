package com.reader.manga.adapters.input.dtos.episode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record EpisodeDTO(
        MultipartFile file,
        @NotNull @NotBlank String id,
        @NotNull @NotBlank String title
) {}
