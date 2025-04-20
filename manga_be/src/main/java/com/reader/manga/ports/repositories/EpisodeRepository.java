package com.reader.manga.ports.repositories;

import com.reader.manga.domain.entities.animes.Episode;

public interface EpisodeRepository {
    Episode save(Episode anime);
}
