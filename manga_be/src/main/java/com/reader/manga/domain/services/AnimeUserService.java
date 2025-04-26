package com.reader.manga.domain.services;

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

    private final UserRepository userRepository;
    private final AnimeService animeService;
    private final AnimeUserRepository animeUserRepository;

    public void criaAssociacaoEntreAnimeUsuario(Long idUser, Long idAnime) {
        User user = userRepository.findById(idUser).orElseThrow(() -> 
            new NotFoundException("Nenhum usuário encontrado com o id: , " + idUser));
            
        Anime anime = animeService.getAnimeById(idAnime);

        AnimeUser animeUser = new AnimeUser(user, anime, null, null, StatusType.ONGOING);

        animeUserRepository.save(animeUser);
    }
}
