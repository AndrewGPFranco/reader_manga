package com.reader.manga.ports.repositories;

import com.reader.manga.domain.entities.animes.VideosComments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VideosCommentsRepository {

    Page<VideosComments> getCommentsByEpisode(Long idEpisode, Pageable pageable);

}
