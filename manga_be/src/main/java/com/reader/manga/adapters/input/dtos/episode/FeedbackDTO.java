package com.reader.manga.adapters.input.dtos.episode;

import lombok.Builder;

@Builder
public record FeedbackDTO(
        Long idEpisode,
        String feedback
) {}
