package com.reader.manga.adapters.input.rest;

import com.reader.manga.adapters.input.dtos.ResponseAPI;
import com.reader.manga.domain.entities.users.User;
import com.reader.manga.domain.valueobjects.animes.AvaliacaoAnimeVO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/add-favorito/{idAnime}")
    public ResponseEntity<ResponseAPI> alteraFavoritoAnime(@PathVariable String idAnime, @AuthenticationPrincipal User user) {
        try {
            service.alteraFavoritoAnime(Long.parseLong(idAnime), user.getId());
            return ResponseEntity.ok().body(new ResponseAPI("Favorito alterado com sucesso", 200));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseAPI("Ocorreu um erro, tente novamente", 400));
        }
    }

}
