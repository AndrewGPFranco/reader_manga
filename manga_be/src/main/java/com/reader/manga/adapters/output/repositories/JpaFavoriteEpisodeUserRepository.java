package com.reader.manga.adapters.output.repositories;

import com.reader.manga.domain.entities.users.FavoriteEpisodeUser;
import com.reader.manga.ports.repositories.FavoriteEpisodeUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaFavoriteEpisodeUserRepository extends JpaRepository<FavoriteEpisodeUser, Long>, FavoriteEpisodeUserRepository {
}
