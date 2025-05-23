package com.reader.manga.adapters.input.rest;

import com.reader.manga.adapters.input.dtos.episode.CommentDTO;
import com.reader.manga.adapters.input.dtos.episode.EpisodeDTO;
import com.reader.manga.adapters.input.dtos.episode.FeedbackDTO;
import com.reader.manga.domain.entities.users.User;
import com.reader.manga.domain.services.EpisodeService;
import com.reader.manga.domain.valueobjects.screens.episodes.EpisodeDisplayVO;
import com.reader.manga.domain.valueobjects.screens.listing.animes.AnimeListingVO;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        UrlResource resource = episodeService.serveVideo(EpisodeDTO.builder().animeId(Long.parseLong(id)).title(anime).build());

        if (resource != null)
            return ResponseEntity.ok()
                    .contentType(MediaTypeFactory.getMediaType(resource).orElse(MediaType.APPLICATION_OCTET_STREAM))
                    .body(resource);
        else
            return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getEpisodeLink(@PathVariable Long id) {
        String link = episodeService.getVideoById(id);

        if (link != null)
            return ResponseEntity.ok().body(link);

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/all/{idAnime}")
    public ResponseEntity<AnimeListingVO> getTodosEpisodiosDoAnime(@PathVariable Long idAnime, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok().body(episodeService.obterEpisodiosDoAnime(idAnime, user.getId()));
    }

    @GetMapping("/get-info-episode/{idEpisode}")
    public ResponseEntity<EpisodeDisplayVO> getEpisodeInfos(@PathVariable Long idEpisode,
                                                            @AuthenticationPrincipal User user) {
        EpisodeDisplayVO episode = episodeService.getEpisodeInfos(idEpisode, user);
        return ResponseEntity.ok().body(episode);
    }

    @PostMapping("/{idEpisode}/update-view")
    public void updateAmountViews(@PathVariable Long idEpisode) {
        try {
            episodeService.updateAmountViews(idEpisode);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Ocorreu um erro ao atualizar contagem de views do episódio com ID: %s", idEpisode));
        }
    }

    @PostMapping("/add-comment")
    public void addComment(@RequestBody CommentDTO dto, @AuthenticationPrincipal User user) {
        try {
            episodeService.addCommentToEpisode(dto, user);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Ocorreu um erro ao adicionar comentário do usuário: %s", user.getUsername()));
        }
    }
    @PostMapping("/handle-feedback")
    public void handleFeedback(@RequestBody FeedbackDTO dto, @AuthenticationPrincipal User user) {
        try {
            episodeService.handleFeedback(dto, user);
        } catch (Exception e) {
            throw new RuntimeException(String.format("Ocorreu um erro ao alterar o feedback do episódio com id: %s", dto.idEpisode()));
        }
    }

}
