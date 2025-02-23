package com.reader.manga.repository;

import com.reader.manga.model.Manga;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MangaRepository extends JpaRepository<Manga, Long> {

    @NotNull Page<Manga> findAll(@NotNull Pageable pageable);
}
