package com.reader.manga.adapters.input.rest;

import com.reader.manga.domain.entities.users.User;
import com.reader.manga.domain.valueobjects.animes.AvaliacaoAnimeVO;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reader.manga.domain.services.AnimeUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/anime/user")
public class AnimeUserController {

    private final AnimeUserService service;

    @PostMapping("/avaliacao")
    public void avaliaAnime(@RequestBody AvaliacaoAnimeVO vo, @AuthenticationPrincipal User user) {
        service.avaliaAnime(vo, user);
    }

}
