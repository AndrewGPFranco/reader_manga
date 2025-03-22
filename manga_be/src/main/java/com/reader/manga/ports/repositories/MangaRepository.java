package com.reader.manga.ports.repositories;

import com.reader.manga.domain.entities.mangas.Manga;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MangaRepository {

    Manga save(Manga manga);

    void deleteById(Long id);

    @NotNull List<Manga> findAll();

    Optional<Manga> findById(Long id);

    Optional<Manga> findByTitle(String title);

    @NotNull Page<Manga> findAll(@NotNull Pageable pageable);

    @NotNull Page<Manga> findByTitle(@NotNull Pageable pageable, String title);

    @NotNull Page<Manga> findByTitleLike(@NotNull String title, @NotNull Pageable pageable);

}