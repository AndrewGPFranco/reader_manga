package com.reader.manga.ports.repositories;

import com.reader.manga.domain.entities.animes.AnimeUser;

public interface AnimeUserRepository {

    AnimeUser save(AnimeUser animeUser);

    Integer getNotaDoAnimeDadoPeloUsuario(Long id, Long idAnime);

}