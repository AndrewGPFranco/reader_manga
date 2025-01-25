package com.reader.manga.repository;

import com.reader.manga.model.FavoriteMangaUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FavoriteMangaRepository extends JpaRepository<FavoriteMangaUser, Long> {}
