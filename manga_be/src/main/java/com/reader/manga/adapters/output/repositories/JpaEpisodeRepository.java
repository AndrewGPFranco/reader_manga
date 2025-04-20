package com.reader.manga.adapters.output.repositories;

import com.reader.manga.domain.entities.animes.Episode;
import com.reader.manga.ports.repositories.EpisodeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaEpisodeRepository extends EpisodeRepository, JpaRepository<Episode, Long> {
}
