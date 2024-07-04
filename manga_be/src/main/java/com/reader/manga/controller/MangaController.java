package com.reader.manga.controller;

import com.reader.manga.dto.GetMangaDTO;
import com.reader.manga.dto.MangaDTO;
import com.reader.manga.service.MangaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/manga")
public class MangaController {

    @Autowired
    private MangaService service;

    private static final Logger logger = LoggerFactory.getLogger(MangaService.class);

    @PostMapping("/create")
    public ResponseEntity<Object> createManga(@RequestBody @Valid MangaDTO dto) {
        try {
            logger.info("*******************Creating mangá!*******************");
            GetMangaDTO manga = service.createManga(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(manga);
        } catch(RuntimeException e) {
            logger.info("*******************Error to create mangá!*******************");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteManga(@PathVariable Long id) {
        try {
            logger.info("*******************Deleting mangá!*******************");
            service.deleteManga(id);
            return ResponseEntity.status(HttpStatus.OK).body("Mangá deleted successfully!");
        } catch(RuntimeException e) {
            logger.info("*******************Mangá not found!*******************");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Mangá not found!");
        }
    }
}
