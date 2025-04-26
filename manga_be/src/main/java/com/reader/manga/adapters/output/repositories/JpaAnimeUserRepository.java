package com.reader.manga.adapters.output.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.reader.manga.domain.entities.animes.AnimeUser;
import com.reader.manga.ports.repositories.AnimeUserRepository;

@Repository
public interface JpaAnimeUserRepository extends JpaRepository<AnimeUser, Long>, AnimeUserRepository {

    @Query("SELECT um.note FROM AnimeUser um WHERE um.userId.id = :id AND um.animeId.id = :idAnime")
    Integer getNotaDoAnimeDadoPeloUsuario(@Param("id") Long id, @Param("idAnime") Long idAnime);

    @Modifying
    @Transactional
    @Query("UPDATE AnimeUser um SET um.note = :nota WHERE um.userId.id = :idUser AND um.animeId.id = :idAnime")
    void atualizaNotaAnime(@Param("idUser") Long idUser, @Param("idAnime") Long idAnime, @Param("nota") Integer nota);

}