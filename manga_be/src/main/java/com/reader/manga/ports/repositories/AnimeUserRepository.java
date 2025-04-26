package com.reader.manga.ports.repositories;

import com.reader.manga.domain.entities.animes.AnimeUser;
import jakarta.validation.constraints.NotNull;

public interface AnimeUserRepository {

    AnimeUser save(AnimeUser animeUser);

    Integer getNotaDoAnimeDadoPeloUsuario(Long id, Long idAnime);

    void atualizaNotaAnime(Long idUser, @NotNull Long idAnime, @NotNull Integer nota);
}