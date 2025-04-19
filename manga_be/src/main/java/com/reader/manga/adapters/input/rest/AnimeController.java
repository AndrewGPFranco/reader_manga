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
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/anime")
public class AnimeController {

    private final AnimeService animeService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadAnime(
                                              @RequestPart("file") MultipartFile file,
                                              @RequestPart("id") String id,
                                              @RequestPart("title") String title
    ) {
        try {
            AnimeDTO dto = new AnimeDTO(file, id, title);
            animeService.uploadAnime(dto);

            return ResponseEntity.ok("Vídeo salvo com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar vídeo: " + e.getMessage());
        }
    }

    @GetMapping("/episode/{id}/{title}")
    public ResponseEntity<UrlResource> getEpisodeAnime(@PathVariable String id, @PathVariable String title) {
        UrlResource resource = animeService.serveVideo(AnimeDTO.builder().id(id).title(title).build());

        if(resource != null)
            return ResponseEntity.ok()
                    .contentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                    .body(resource);
        else
            return ResponseEntity.notFound().build();
    }

}
