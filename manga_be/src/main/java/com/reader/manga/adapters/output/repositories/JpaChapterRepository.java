package com.reader.manga.adapters.output.repositories;

import com.reader.manga.domain.entities.mangas.Chapter;
import com.reader.manga.ports.repositories.ChapterRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaChapterRepository extends ChapterRepository, JpaRepository<Chapter, Long> {}