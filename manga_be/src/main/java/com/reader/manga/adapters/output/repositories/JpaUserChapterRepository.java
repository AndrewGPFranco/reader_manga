package com.reader.manga.adapters.output.repositories;

import com.reader.manga.domain.entities.users.UserChapter;
import com.reader.manga.ports.repositories.UserChapterRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaUserChapterRepository extends UserChapterRepository, JpaRepository<UserChapter, Long> {

    @Query("SELECT u FROM UserChapter u WHERE u.chapter_id.id = :idChapter AND u.user_id.id = :idUser")
    UserChapter findByIdChapterAndUser(@Param("idChapter") Long idChapter, @Param("idUser") Long idUser);

    @Query("SELECT u FROM UserChapter u WHERE u.progress > 0 and u.user_id.id = :idUser and u.status != 'FINISHED'")
    Page<UserChapter> findAllReadingsInProgress(Pageable pageable, @Param("idUser") Long idUser);

}