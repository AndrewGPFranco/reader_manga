package com.reader.manga.domain.valueobjects.screens.episodes;

import lombok.Builder;

@Builder
public record EpisodeCommentsVO(
        String nameUser,
        String comment
) {}
