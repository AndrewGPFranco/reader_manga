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

    @Query("SELECT COUNT(um) FROM user_manga um WHERE um.user.id = :id")
    Integer quantidadeMangasAssinadosPeloUsuario(@Param("id") Long id);

    @Query("SELECT um.nota FROM user_manga um WHERE um.user.id = :id AND um.manga.id = :idManga")
    Long getNotaDoMangaDadoPeloUsuario(@Param("id") Long id, @Param("idManga") Long idManga);

    @Modifying
    @Transactional
    @Query("UPDATE user_manga SET nota = :nota WHERE user.id = :idUser AND manga.id = :idManga")
    void atualizaNotaManga(@Param("idUser") Long idUser, @Param("idManga") Long idManga, @Param("nota") Integer nota);


}