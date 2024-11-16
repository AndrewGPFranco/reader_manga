package com.reader.manga.controller;

import com.reader.manga.dto.manga.GetMangaDTO;
import com.reader.manga.dto.manga.MangaDTO;
import com.reader.manga.dto.manga.UpdateMangaDTO;
import com.reader.manga.model.Manga;
import com.reader.manga.service.MangaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manga")
public class MangaController {

    private final MangaService service;

    public MangaController(MangaService service) {
        this.service = service;
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
}
