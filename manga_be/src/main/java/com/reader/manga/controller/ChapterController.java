package com.reader.manga.controller;

import com.reader.manga.dto.ChapterDTO;
import com.reader.manga.dto.GetMangaDTO;
import com.reader.manga.model.Chapter;
import com.reader.manga.service.ChapterService;
import com.reader.manga.service.MangaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chapter")
public class ChapterController {

    private static final Logger logger = LoggerFactory.getLogger(MangaService.class);

    @Autowired
    private ChapterService service;

    @PostMapping("create")
    public ResponseEntity<?> createChapter(@RequestBody ChapterDTO dto, Long id) {
        try {
            logger.info("*******************Creating mangá!*******************");
            Chapter chapter = service.createChapter(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(chapter);
        } catch(RuntimeException e) {
            logger.error("*******************Error to create mangá!*******************");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("readAll")
    public List<Chapter> readAllChapters() {
        return service.listOfChapters();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteManga(@PathVariable Long id) {
        try {
            logger.info("*******************Deleting mangá!*******************");
            service.deleteManga(id);
            return ResponseEntity.status(HttpStatus.OK).body("Chapter deleted successfully!");
        } catch(RuntimeException e) {
            logger.error("*******************Mangá not found!*******************");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Chapter not found!");
        }
    }
}
