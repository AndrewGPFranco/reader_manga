package com.reader.manga.repository;

import com.reader.manga.model.Manga;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MangaRepository extends JpaRepository<Manga, Long> {

    @NotNull Page<Manga> findAll(@NotNull Pageable pageable);

    Optional<Manga> findByTitle(String title);

    @NotNull Page<Manga> findByTitle(@NotNull Pageable pageable, String title);

    @Query("select m from Manga m where LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    @NotNull Page<Manga> findByTitleLike(@NotNull String title, @NotNull Pageable pageable);

}