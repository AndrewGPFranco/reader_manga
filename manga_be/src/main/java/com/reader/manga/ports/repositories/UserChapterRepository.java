package com.reader.manga.ports.repositories;

import com.reader.manga.domain.entities.users.UserChapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserChapterRepository {

    UserChapter save(UserChapter userChapter);

    Optional<UserChapter> findById(Long id);

    UserChapter findByIdChapterAndUser(Long idChapter, Long idUser);

    Page<UserChapter> findAllReadingsInProgress(Pageable pageable, Long idUser);

    void deleteAssociacao(Long idChapter, Long idUser);
}