package com.reader.manga.adapters.input.rest;

import com.reader.manga.adapters.input.dtos.episode.EpisodeDTO;
import com.reader.manga.domain.services.EpisodeService;
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
@RequestMapping("/api/v1/episode")
public class EpisodeController {

    private final EpisodeService episodeService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadEpisode(
            @RequestPart("file") MultipartFile file,
            @RequestPart("id") String id,
            @RequestPart("title") String title,
            @RequestPart("anime") String anime
    ) {
        try {
            EpisodeDTO dto = new EpisodeDTO(file, id, title, Long.parseLong(anime));
            episodeService.uploadAnime(dto);

            return ResponseEntity.ok("Vídeo salvo com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao salvar vídeo: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/{anime}")
    public ResponseEntity<UrlResource> getEpisodeAnime(@PathVariable String id, @PathVariable String anime) {
        UrlResource resource = episodeService.serveVideo(EpisodeDTO.builder().id(id).title(anime).build());

        if(resource != null)
            return ResponseEntity.ok()
                    .contentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                    .body(resource);
        else
            return ResponseEntity.notFound().build();
    }

}
