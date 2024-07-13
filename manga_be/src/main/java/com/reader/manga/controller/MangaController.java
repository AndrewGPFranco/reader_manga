package com.reader.manga.controller;

import com.reader.manga.dto.GetMangaDTO;
import com.reader.manga.dto.MangaDTO;
import com.reader.manga.dto.UpdateMangaDTO;
import com.reader.manga.model.Manga;
import com.reader.manga.service.MangaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/manga")
public class MangaController {

    @Autowired
    private MangaService service;

    private static final Logger logger = LoggerFactory.getLogger(MangaService.class);

    @PostMapping("/create")
    public ResponseEntity<?> createManga(@RequestBody @Valid MangaDTO dto) {
        try {
            logger.info("*******************Creating mangá!*******************");
            GetMangaDTO manga = service.createManga(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(manga);
        } catch(RuntimeException e) {
            logger.error("*******************Error to create mangá!*******************");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteManga(@PathVariable Long id) {
        try {
            logger.info("*******************Deleting mangá!*******************");
            service.deleteManga(id);
            return ResponseEntity.status(HttpStatus.OK).body("Mangá deleted successfully!");
        } catch(RuntimeException e) {
            logger.error("*******************Mangá not found!*******************");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mangá not found!");
        }
    }

    @GetMapping("/readAll")
    public ResponseEntity<List<Manga>> readAllMangas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("*******************Reading all mangas!*******************");
            Pageable pageable = PageRequest.of(page, size);
            List<Manga> mangas = service.readAllMangas(pageable);
            return ResponseEntity.status(HttpStatus.OK).body(mangas);
        } catch (RuntimeException e) {
            logger.error("*******************Error to read all mangas!*******************");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping("/edit")
    public ResponseEntity<String> updateMangaById(@RequestParam Long id, @RequestBody UpdateMangaDTO dto) {
        try {
            service.updateManga(id, dto);
            logger.info("*******************Updating mangá!*******************");
            return ResponseEntity.status(HttpStatus.OK).body("Mangá updated successfully!");
        } catch (RuntimeException e) {
            logger.error("*******************Error to update mangá!*******************");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
