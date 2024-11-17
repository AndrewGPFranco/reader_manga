package com.reader.manga.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.reader.manga.dto.manga.GetMangaDTO;
import com.reader.manga.dto.manga.MangaDTO;
import com.reader.manga.dto.manga.UpdateMangaDTO;
import com.reader.manga.model.Chapter;
import com.reader.manga.model.Manga;
import com.reader.manga.service.MangaService;
import com.reader.manga.vo.MangaCoverVO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manga")
public class MangaController {

    private final MangaService service;

    private final WebClient webClient;

    public MangaController(MangaService service, WebClient webClient) {
        this.service = service;
        this.webClient = webClient;
    }

    private static final Logger logger = LoggerFactory.getLogger(MangaController.class);

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/read/{id}")
    public ResponseEntity<Manga> getMangaById(@PathVariable Long id) {
        logger.info("*******************Reading manga!*******************");
        Manga manga = service.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(manga);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Object> createManga(@RequestBody @Valid MangaDTO dto) {
        logger.info("*******************Creating mangá!*******************");
        GetMangaDTO manga = service.createManga(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(manga);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteManga(@PathVariable Long id) {
        logger.info("*******************Deleting mangá!*******************");
        service.deleteManga(id);
        return ResponseEntity.status(HttpStatus.OK).body("Mangá deleted successfully!");
    }

    @GetMapping("/readAll")
    public ResponseEntity<List<Manga>> readAllMangas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("*******************Reading all mangas!*******************");
        Pageable pageable = PageRequest.of(page, size);
        List<Manga> mangas = service.readAllMangas(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(mangas);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/edit")
    public ResponseEntity<String> updateMangaById(@RequestParam Long id, @RequestBody UpdateMangaDTO dto) {
        service.updateManga(id, dto);
        logger.info("*******************Updating mangá!*******************");
        return ResponseEntity.status(HttpStatus.OK).body("Mangá updated successfully!");
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllMangaForSelect() {
        List<Manga> allMangas = service.getAll();
        return ResponseEntity.ok().body(allMangas);
    }

    @GetMapping("/chapter_id/by-id/{id}")
    public ResponseEntity<List<Chapter>> getChapterByManga(@PathVariable Long id) {
        List<Chapter> chapters = service.getChaptersByManga(id);
        return ResponseEntity.ok().body(chapters);
    }

    @GetMapping("/get-covers")
    public Mono<List<MangaCoverVO>> getMangaCovers() {
        List<String> titles = List.of("Naruto", "Death Note", "One Piece", "Fullmetal Alchemist", "Dragon Ball");

        return Flux.fromIterable(titles)
                .flatMap(this::fetchCoverForTitle)
                .take(5)
                .collectList();
    }

    // TODO passar lógica para um service
    private Mono<MangaCoverVO> fetchCoverForTitle(String title) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/manga")
                        .queryParam("title", title)
                        .queryParam("limit", 1)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .flatMap(jsonNode -> {
                    JsonNode data = jsonNode.get("data");
                    if (data != null && !data.isEmpty()) {
                        JsonNode manga = data.get(0);
                        String mangaId = manga.get("id").asText();
                        return fetchCoverImage(mangaId);
                    }
                    return Mono.empty();
                });
    }

    // TODO passar lógica para um service
    private Mono<MangaCoverVO> fetchCoverImage(String mangaId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/cover")
                        .queryParam("manga[]", mangaId)
                        .queryParam("limit", 1)
                        .build())
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(jsonNode -> {
                    JsonNode data = jsonNode.get("data");
                    if (data != null && !data.isEmpty()) {
                        JsonNode cover = data.get(0);
                        String fileName = cover.get("attributes").get("fileName").asText();
                        String imageUrl = "https://uploads.mangadex.org/covers/" + mangaId + "/" + fileName;

                        return MangaCoverVO.builder().id(mangaId).imageUrl(imageUrl).build();
                    }
                    return null;
                });
    }
}
