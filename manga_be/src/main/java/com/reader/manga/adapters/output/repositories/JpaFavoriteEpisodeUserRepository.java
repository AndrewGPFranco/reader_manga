package com.reader.manga.adapters.output.repositories;

import com.reader.manga.domain.entities.users.FavoriteEpisodeUser;
import com.reader.manga.ports.repositories.FavoriteEpisodeUserRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface JpaFavoriteEpisodeUserRepository extends JpaRepository<FavoriteEpisodeUser, Long>, FavoriteEpisodeUserRepository {

    @Query("select fe.feedback from FavoriteEpisodeUser fe where fe.user.id = :idUser and fe.episode.id = :idEpisode")
    String getFeedbackByUserAndEpisode(@Param("idUser") Long idUser, @Param("idEpisode") Long idEpisode);

    @Query("select fe from FavoriteEpisodeUser fe where fe.user.id = :idUser and fe.episode.id = :idEpisode")
    FavoriteEpisodeUser favoriteEpisodeByUser(Long idUser, Long idEpisode);

}
