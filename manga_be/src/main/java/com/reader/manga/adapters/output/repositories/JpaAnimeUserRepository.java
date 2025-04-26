package com.reader.manga.adapters.output.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.reader.manga.domain.entities.animes.AnimeUser;
import com.reader.manga.ports.repositories.AnimeUserRepository;

@Repository
public interface JpaAnimeUserRepository extends JpaRepository<AnimeUser, Long>, AnimeUserRepository {}