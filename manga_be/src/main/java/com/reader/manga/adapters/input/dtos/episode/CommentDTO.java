package com.reader.manga.adapters.input.dtos.episode;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record CommentDTO(
        @NotNull Long idEpisode,
        @NotBlank String comment
) {}
