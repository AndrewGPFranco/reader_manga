package com.reader.manga.ports.repositories;

import com.reader.manga.domain.entities.animes.Anime;

import java.util.List;
import java.util.Optional;

public interface AnimeRepository {

    Anime save(Anime anime);

    List<Anime> findAll();

    Optional<Anime> findById(Long id);
}