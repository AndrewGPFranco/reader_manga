package com.reader.manga.ports.repositories;

import com.reader.manga.domain.entities.users.FavoriteAnimeUser;

public interface FavoriteAnimeRepository {

    FavoriteAnimeUser save(FavoriteAnimeUser favoriteAnimeUser);

    FavoriteAnimeUser favoriteIsTrue(Long idAnime, Long idUser);

    void removerAnimeDaListaDeFavoritos(Long idAnime, Long idUser);

}