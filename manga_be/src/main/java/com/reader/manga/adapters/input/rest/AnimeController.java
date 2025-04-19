package com.reader.manga.adapters.input.rest;

import com.reader.manga.adapters.input.dtos.ResponseAPI;
import com.reader.manga.adapters.input.dtos.anime.AnimeDTO;
import com.reader.manga.adapters.input.dtos.episode.EpisodeDTO;
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

    @PostMapping
    public ResponseEntity<ResponseAPI> registrarAnime(@RequestBody AnimeDTO dto) {
        try {
            animeService.registraAnime(dto);
            return ResponseEntity.ok().body(new ResponseAPI("Registrado com sucesso!", HttpStatus.CREATED.value()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseAPI(e.getMessage(), HttpStatus.BAD_REQUEST.value()));
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadAnime(
                                              @RequestPart("file") MultipartFile file,
                                              @RequestPart("id") String id,
                                              @RequestPart("title") String title
    ) {
        try {
            EpisodeDTO dto = new EpisodeDTO(file, id, title);
            animeService.uploadAnime(dto);

            return ResponseEntity.ok("Vídeo salvo com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar vídeo: " + e.getMessage());
        }
    }

    @GetMapping("/episode/{id}/{anime}")
    public ResponseEntity<UrlResource> getEpisodeAnime(@PathVariable String id, @PathVariable String anime) {
        UrlResource resource = animeService.serveVideo(EpisodeDTO.builder().id(id).title(anime).build());

        if(resource != null)
            return ResponseEntity.ok()
                    .contentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                    .body(resource);
        else
            return ResponseEntity.notFound().build();
    }

}
