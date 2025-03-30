package com.reader.manga.adapters.output.repositories;

import com.reader.manga.domain.entities.users.UserChapter;
import com.reader.manga.ports.repositories.UserChapterRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserChapterRepository extends UserChapterRepository, JpaRepository<UserChapter, Long> {}