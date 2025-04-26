package com.reader.manga.adapters.input.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.reader.manga.adapters.input.dtos.AnimeUserDTO;
import com.reader.manga.domain.services.AnimeUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/anime/user")
public class AnimeUserController {

    private final AnimeUserService service;

    @PostMapping
    public ResponseEntity<Object> save(@RequestBody AnimeUserDTO dto) {
        try {
            service.criaAssociacaoEntreAnimeUsuario(dto.idUser(), dto.idAnime());

            return ResponseEntity.status(HttpStatus.CREATED).body("Associação criada com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
