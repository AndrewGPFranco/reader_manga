package com.reader.manga.adapters.output.repositories;

import com.reader.manga.domain.entities.users.FavoriteAnimeUser;
import com.reader.manga.ports.repositories.FavoriteAnimeRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaFavoriteAnimeRepository extends FavoriteAnimeRepository, JpaRepository<FavoriteAnimeUser, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM anime_favorite_user fa WHERE fa.anime.id = :idAnime AND fa.user.id = :idUser")
    void removerAnimeDaListaDeFavoritos(@Param("idAnime") Long idAnime, @Param("idUser") Long idUser);

    @Transactional
    @Query("SELECT fa FROM anime_favorite_user fa WHERE fa.anime.id = :idAnime AND fa.user.id = :idUser")
    FavoriteAnimeUser favoriteIsTrue(@Param("idAnime") Long idAnime, @Param("idUser") Long idUser);

}
