package com.reader.manga.domain.valueobjects.screens.listing.animes;

import lombok.Builder;

@Builder
public record EpisodeToAnimesVO(
        Long id,
        String uriEpisode,
        String titleEpisode,
        Integer numberEpisode
) {}