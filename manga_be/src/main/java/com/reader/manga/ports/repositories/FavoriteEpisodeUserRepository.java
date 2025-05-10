package com.reader.manga.ports.repositories;

public interface FavoriteEpisodeUserRepository {

    String getFeedbackByUserAndEpisode(Long idUser, Long idEpisode);

}
