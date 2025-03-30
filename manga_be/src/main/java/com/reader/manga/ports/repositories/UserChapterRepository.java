package com.reader.manga.ports.repositories;

import com.reader.manga.domain.entities.users.UserChapter;

import java.util.Optional;

public interface UserChapterRepository {

    UserChapter save(UserChapter userChapter);

    Optional<UserChapter> findById(Long id);

    UserChapter findByIdChapterAndUser(Long idChapter, Long idUser);

}