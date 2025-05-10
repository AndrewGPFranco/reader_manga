package com.reader.manga.adapters.output.repositories;

import com.reader.manga.domain.entities.animes.VideosComments;
import com.reader.manga.ports.repositories.VideosCommentsRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaVideosCommentsRepository extends JpaRepository<VideosComments, Long>, VideosCommentsRepository {

    @Query("select vc from VideosComments vc where vc.episode.id = :idEpisode")
    Page<VideosComments> getCommentsByEpisode(@Param("idEpisode") Long idEpisode, Pageable pageable);

    @Query("select vc from VideosComments vc where vc.episode.id = :idEpisode")
    List<VideosComments> getCommentsByEpisode(Long idEpisode);

}
