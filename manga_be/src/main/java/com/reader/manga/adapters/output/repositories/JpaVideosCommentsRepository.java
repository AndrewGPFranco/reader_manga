package com.reader.manga.adapters.output.repositories;

import com.reader.manga.domain.entities.animes.VideosComments;
import com.reader.manga.ports.repositories.VideosCommentsRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaVideosCommentsRepository extends JpaRepository<VideosComments, Long>, VideosCommentsRepository {
}
