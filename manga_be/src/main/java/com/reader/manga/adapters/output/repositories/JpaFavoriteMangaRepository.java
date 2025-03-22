package com.reader.manga.adapters.output.repositories;

import com.reader.manga.domain.entities.users.FavoriteMangaUser;
import com.reader.manga.ports.repositories.FavoriteMangaRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaFavoriteMangaRepository extends FavoriteMangaRepository, JpaRepository<FavoriteMangaUser, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM manga_favorite_user fm WHERE fm.manga.id = :idManga AND fm.user.id = :idUser")
    void removerMangaDaListaDeFavoritos(@org.springframework.data.repository.query.Param("idManga") Long idManga, @Param("idUser") Long idUser);

    @Transactional
    @Query("SELECT fm FROM manga_favorite_user fm WHERE fm.manga.id = :idManga AND fm.user.id = :idUser")
    FavoriteMangaUser favoriteIsTrue(@Param("idManga") Long idManga, @Param("idUser") Long idUser);

}