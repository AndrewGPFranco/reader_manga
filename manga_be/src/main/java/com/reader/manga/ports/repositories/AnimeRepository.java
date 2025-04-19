package com.reader.manga.ports.repositories;

import com.reader.manga.domain.entities.animes.Anime;

public interface AnimeRepository {

    Anime save(Anime anime);

}