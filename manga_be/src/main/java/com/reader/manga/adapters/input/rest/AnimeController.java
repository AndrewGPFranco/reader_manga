package com.reader.manga.adapters.input.rest;

import com.reader.manga.adapters.input.dtos.animes.AnimeDTO;
import com.reader.manga.domain.services.AnimeService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/anime")
public class AnimeController {

    private final AnimeService animeService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadAnime(@RequestBody AnimeDTO dto) {
        try {
            animeService.uploadAnime(dto);

            return ResponseEntity.ok("Vídeo salvo com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar vídeo: " + e.getMessage());
        }
    }

    @GetMapping("/video")
    public ResponseEntity<UrlResource> getEpisodeAnime(@RequestBody AnimeDTO dto) {
        UrlResource resource = animeService.serveVideo(dto);

        if(resource != null)
            return ResponseEntity.ok()
                    .contentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                    .body(resource);
        else
            return ResponseEntity.notFound().build();
    }

}
