package com.reader.manga.ports.repositories;

import com.reader.manga.domain.entities.users.FavoriteMangaUser;

public interface FavoriteMangaRepository {

    FavoriteMangaUser save(FavoriteMangaUser favoriteMangaUser);

    FavoriteMangaUser favoriteIsTrue(Long idManga, Long idUser);

    void removerMangaDaListaDeFavoritos(Long idManga, Long idUser);

}