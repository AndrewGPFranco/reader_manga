package com.reader.manga.adapters.output.repositories;

import com.reader.manga.domain.entities.mangas.History;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface JPAHistoryRepository extends JpaRepository<History, UUID> {

    @Query("select h from History h where h.idUser = :idUser and h.idCapitulo = :idChapter and h.idManga = :idManga")
    List<History> getAllHistoryByUserChapterAndManga(@Param("idUser") Long idUser, @Param("idChapter") Long idChapter, @Param("idManga") Long idManga);

    @Query("select h from History h where h.idUser = :idUser")
    Page<History> findAllByUser(@Param("idUser") Long id, PageRequest of);
}
