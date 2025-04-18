package com.reader.manga.adapters.output.repositories;

import com.reader.manga.domain.entities.animes.Anime;
import com.reader.manga.ports.repositories.AnimeRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaAnimeRepository extends AnimeRepository, JpaRepository<Anime, Long> {}
