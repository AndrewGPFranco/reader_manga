package com.reader.manga.ports.repositories;

import com.reader.manga.domain.entities.users.UserManga;

public interface UserMangaRepository {

    UserManga save(UserManga user);

    void deletaAssociacao(Long idUser, Long idManga);

    Integer quantidadeMangasAssinadosPeloUsuario(Long id);

    Long getNotaDoMangaDadoPeloUsuario(Long id, Long idManga);

    void atualizaNotaManga(Long idUser, Long idManga, Integer nota);

    Integer mangaIsInUserLibrary(Long idManga, Long idUser);
}