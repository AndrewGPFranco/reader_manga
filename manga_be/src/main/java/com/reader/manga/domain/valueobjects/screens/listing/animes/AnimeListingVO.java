package com.reader.manga.domain.valueobjects.screens.listing.animes;

import com.reader.manga.domain.enums.TagType;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

/**
 * VO para centralizar dados a ser mandado para tela de listagem de animes.
 * @author andrewgo
 */
@Builder
public record AnimeListingVO(
        Long idAnime,
        Integer note,
        String uriImage,
        String titleAnime,
        LocalDate launchYear,
        Boolean isFavorite,
        List<TagType> tags,
        List<EpisodeToAnimesVO> episodes
) {}