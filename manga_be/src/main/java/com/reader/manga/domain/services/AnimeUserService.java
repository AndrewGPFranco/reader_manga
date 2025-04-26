package com.reader.manga.domain.services;

import com.reader.manga.domain.entities.users.FavoriteAnimeUser;
import com.reader.manga.domain.valueobjects.animes.AvaliacaoAnimeVO;
import com.reader.manga.ports.repositories.FavoriteAnimeRepository;
import org.springframework.stereotype.Service;

import com.reader.manga.domain.entities.animes.Anime;
import com.reader.manga.domain.entities.users.AnimeUser;
import com.reader.manga.domain.entities.users.User;
import com.reader.manga.domain.enums.StatusType;
import com.reader.manga.domain.exceptions.NotFoundException;
import com.reader.manga.ports.repositories.AnimeUserRepository;
import com.reader.manga.ports.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AnimeUserService {

    private final AnimeService animeService;
    private final UserRepository userRepository;
    private final AnimeUserRepository animeUserRepository;
    private final FavoriteAnimeRepository favoriteAnimeRepository;

    public void criaAssociacaoEntreAnimeUsuario(Long idUser, Long idAnime, Integer nota, Integer progress) {
        User user = userRepository.findById(idUser).orElseThrow(() -> 
            new NotFoundException("Nenhum usuário encontrado com o id: , " + idUser));
            
        Anime anime = animeService.getAnimeById(idAnime);

        AnimeUser animeUser = new AnimeUser(user, anime, nota, progress, StatusType.ONGOING);

        animeUserRepository.save(animeUser);
    }

    public void alteraFavoritoAnime(Long idAnime, Long idUser) {
        try {
            Anime anime = animeService.getAnimeById(idAnime);
            Optional<User> usuario = userRepository.findById(idUser);

            FavoriteAnimeUser isFavorito = favoriteAnimeRepository.favoriteIsTrue(idAnime, idUser);

            if(isFavorito != null) {
                favoriteAnimeRepository.removerAnimeDaListaDeFavoritos(idAnime, idUser);
            } else {
                if(anime != null && usuario.isPresent()) {
                    FavoriteAnimeUser favoriteAnimeUser = FavoriteAnimeUser.builder()
                            .anime(anime)
                            .user(usuario.get())
                            .build();
                    favoriteAnimeRepository.save(favoriteAnimeUser);
                }
            }
        } catch (NotFoundException e) {
            throw new NotFoundException("Argumento não encontrado, verifique as informações");
        }
    }

    public Integer getNotaPeloUsuario(Long idUser, Long idAnime) {
        return animeUserRepository.getNotaDoAnimeDadoPeloUsuario(idUser, idAnime);
    }

    public void avaliaAnime(AvaliacaoAnimeVO vo, User user) {
        Integer notaDoAnimeDadoPeloUsuario = animeUserRepository.getNotaDoAnimeDadoPeloUsuario(user.getId(), vo.idAnime());
        if(notaDoAnimeDadoPeloUsuario != null)
            animeUserRepository.atualizaNotaAnime(user.getId(), vo.idAnime(), vo.nota());
        else
            criaAssociacaoEntreAnimeUsuario(user.getId(), vo.idAnime(), vo.nota(), null);
    }

}
