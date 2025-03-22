package com.reader.manga.ports.repositories;

import com.reader.manga.domain.entities.users.UserManga;

public interface UserMangaRepository {

    UserManga save(UserManga user);

    void deletaAssociacao(Long idUser, Long idManga);

}