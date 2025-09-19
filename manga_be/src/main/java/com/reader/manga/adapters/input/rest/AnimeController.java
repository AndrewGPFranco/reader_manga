package com.reader.manga.adapters.input.rest;

import com.reader.manga.adapters.input.dtos.ResponseAPI;
import com.reader.manga.adapters.input.dtos.anime.AnimeDTO;
import com.reader.manga.domain.entities.animes.Anime;
import com.reader.manga.domain.services.AnimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/anime")
public class AnimeController {

    private final AnimeService animeService;

    @PostMapping
    public ResponseEntity<ResponseAPI> registrarAnime(@RequestBody AnimeDTO dto) {
        try {
            animeService.registraAnime(dto);
            return ResponseEntity.ok().body(ResponseAPI.builder()
                            .message("Registrado com sucesso!")
                            .statusCode(HttpStatus.CREATED.value()).build());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ResponseAPI.builder()
                    .message(e.getMessage())
                    .statusCode(HttpStatus.BAD_REQUEST.value()).build());
        }
    }

    @GetMapping
    public ResponseEntity<List<Anime>> getTodosAnimes() {
        return ResponseEntity.ok().body(animeService.getAllAnimes());
    }

    @GetMapping("/{id}/get-image")
    public ResponseEntity<String> getUriImage(@PathVariable Long id) {
        String imageByAnime = animeService.getImageByAnime(id);

        return ResponseEntity.ok().body(imageByAnime);
    }

}
