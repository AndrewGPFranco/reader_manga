package com.reader.manga.repository;

import com.reader.manga.model.FavoriteMangaUser;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteMangaRepository extends JpaRepository<FavoriteMangaUser, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM manga_favorite_user fm WHERE fm.manga.id = :idManga AND fm.user.id = :idUser")
    void removerMangaDaListaDeFavoritos(@org.springframework.data.repository.query.Param("idManga") Long idManga, @Param("idUser") Long idUser);

    @Transactional
    @Query("SELECT fm FROM manga_favorite_user fm WHERE fm.manga.id = :idManga AND fm.user.id = :idUser")
    FavoriteMangaUser favoriteIsTrue(@Param("idManga") Long idManga, @Param("idUser") Long idUser);


}
