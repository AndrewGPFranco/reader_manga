package com.reader.manga.adapters.output.repositories;

import com.reader.manga.domain.entities.animes.Episode;
import com.reader.manga.ports.repositories.EpisodeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaEpisodeRepository extends EpisodeRepository, JpaRepository<Episode, Long> {

    @Query("SELECT e FROM Episode e WHERE e.anime.id = :idAnime")
    List<Episode> findByIdAndAnime(@Param("idAnime") Long idAnime);

}
