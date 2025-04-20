package com.reader.manga.ports.repositories;

import com.reader.manga.domain.entities.animes.Episode;

import java.util.List;
import java.util.Optional;

public interface EpisodeRepository {

    Episode save(Episode anime);

    Optional<Episode> findById(Long id);

    List<Episode> findByIdAndAnime(Long idAnime);
}
