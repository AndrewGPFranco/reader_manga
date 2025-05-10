package com.reader.manga.domain.valueobjects.screens.episodes;

import com.reader.manga.domain.enums.FeedbackEpisodeType;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

@Builder
public record EpisodeDisplayVO(
        String uriEpisode,
        String titleEpisode,
        Integer amountViews,
        LocalDate uploaded,
        FeedbackEpisodeType feedback,
        Page<EpisodeCommentsVO> commentsList
) {}
