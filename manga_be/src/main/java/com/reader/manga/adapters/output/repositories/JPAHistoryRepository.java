package com.reader.manga.adapters.output.repositories;

import com.reader.manga.domain.entities.mangas.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JPAHistoryRepository extends JpaRepository<History, UUID> {

    @Query("select h from History h where h.user_id = :idUser and h.chapter_id = :idChapter and h.manga_id = :idManga")
    History getHistory(@Param("idUser") Long idUser, @Param("idChapter") Long idChapter, @Param("idManga") Long idManga);

}
