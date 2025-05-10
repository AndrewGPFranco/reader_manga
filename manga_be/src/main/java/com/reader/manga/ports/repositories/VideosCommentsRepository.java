package com.reader.manga.ports.repositories;

import com.reader.manga.domain.entities.animes.VideosComments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VideosCommentsRepository {

    Page<VideosComments> getCommentsByEpisode(Long idEpisode, Pageable pageable);

    List<VideosComments> getCommentsByEpisode(Long idEpisode);

    VideosComments save(VideosComments comment);

}
