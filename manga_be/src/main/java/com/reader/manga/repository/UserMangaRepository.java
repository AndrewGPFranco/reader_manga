package com.reader.manga.repository;

import com.reader.manga.model.UserManga;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMangaRepository extends JpaRepository<UserManga, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM user_manga um WHERE um.manga.id = :idManga AND um.user.id = :idUser")
    void deletaAssociacao(@Param("idUser") Long idUser, @Param("idManga") Long idManga);

}
