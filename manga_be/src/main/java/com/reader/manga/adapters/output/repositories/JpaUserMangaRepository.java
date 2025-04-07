package com.reader.manga.adapters.output.repositories;

import com.reader.manga.domain.entities.users.UserManga;
import com.reader.manga.ports.repositories.UserMangaRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaUserMangaRepository extends UserMangaRepository, JpaRepository<UserManga, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM user_manga um WHERE um.manga.id = :idManga AND um.user.id = :idUser")
    void deletaAssociacao(@Param("idUser") Long idUser, @Param("idManga") Long idManga);

    @Query("SELECT COUNT(u) FROM UserChapter u WHERE u.user_id.id = :id")
    Integer mangaNumberSignedByUser(@Param("id") Long id);

}