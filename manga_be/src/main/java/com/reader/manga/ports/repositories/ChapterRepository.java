package com.reader.manga.ports.repositories;

import com.reader.manga.domain.entities.mangas.Chapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChapterRepository {

    List<Chapter> findAll();

    void deleteById(Long id);

    Chapter save(Chapter chapter);

    Optional<Chapter> findById(Long id);

    Page<Chapter> findAll(Pageable pageable);

    @Query("SELECT u FROM Chapter u WHERE u.readingProgress > 0 and u.status != 'FINISHED'")
    Page<Chapter> findAllReadingsInProgress(Pageable pageable);

}