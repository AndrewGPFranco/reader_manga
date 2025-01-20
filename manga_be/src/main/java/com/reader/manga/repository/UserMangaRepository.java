package com.reader.manga.repository;

import com.reader.manga.model.UserManga;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMangaRepository extends JpaRepository<UserManga, Long> {}
