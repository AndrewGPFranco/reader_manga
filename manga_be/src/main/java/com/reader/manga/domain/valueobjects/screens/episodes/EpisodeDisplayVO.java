package com.reader.manga.domain.valueobjects.screens.episodes;

import com.reader.manga.domain.enums.FeedbackEpisodeType;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
public record EpisodeDisplayVO(
        String uriEpisode,
        String titleEpisode,
        Integer amountViews,
        LocalDate uploaded,
        FeedbackEpisodeType feedback,
        List<EpisodeCommentsVO> commentsList,
        String uriPath
) {}
