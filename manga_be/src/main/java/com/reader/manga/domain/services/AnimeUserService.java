package com.reader.manga.domain.services;

import com.reader.manga.domain.valueobjects.animes.AvaliacaoAnimeVO;
import org.springframework.stereotype.Service;

import com.reader.manga.domain.entities.animes.Anime;
import com.reader.manga.domain.entities.animes.AnimeUser;
import com.reader.manga.domain.entities.users.User;
import com.reader.manga.domain.enums.StatusType;
import com.reader.manga.domain.exceptions.NotFoundException;
import com.reader.manga.ports.repositories.AnimeUserRepository;
import com.reader.manga.ports.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimeUserService {

    private final AnimeService animeService;
    private final UserRepository userRepository;
    private final AnimeUserRepository animeUserRepository;

    public void criaAssociacaoEntreAnimeUsuario(Long idUser, Long idAnime, Integer nota, Integer progress) {
        User user = userRepository.findById(idUser).orElseThrow(() -> 
            new NotFoundException("Nenhum usuário encontrado com o id: , " + idUser));
            
        Anime anime = animeService.getAnimeById(idAnime);

        AnimeUser animeUser = new AnimeUser(user, anime, nota, progress, StatusType.ONGOING);

        animeUserRepository.save(animeUser);
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
