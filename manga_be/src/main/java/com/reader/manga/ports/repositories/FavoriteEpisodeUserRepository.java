package com.reader.manga.ports.repositories;

import com.reader.manga.domain.entities.users.FavoriteEpisodeUser;

public interface FavoriteEpisodeUserRepository {

    String getFeedbackByUserAndEpisode(Long idUser, Long idEpisode);

    FavoriteEpisodeUser save(FavoriteEpisodeUser favoriteEpisodeUser);

    FavoriteEpisodeUser favoriteEpisodeByUser(Long idUser, Long idEpisode);

    void deleteById(Long id);

}
