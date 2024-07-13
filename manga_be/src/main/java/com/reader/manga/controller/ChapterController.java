package com.reader.manga.controller;

import com.reader.manga.dto.ChapterDTO;
import com.reader.manga.dto.GetMangaDTO;
import com.reader.manga.dto.UpdateChapterDTO;
import com.reader.manga.dto.UpdateMangaDTO;
import com.reader.manga.model.Chapter;
import com.reader.manga.model.Manga;
import com.reader.manga.service.ChapterService;
import com.reader.manga.service.MangaService;
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
@RequestMapping("/api/v1/chapter")
public class ChapterController {

    private static final Logger logger = LoggerFactory.getLogger(MangaService.class);

    @Autowired
    private ChapterService service;

    @PostMapping("/create")
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

    @GetMapping("/readAll")
    public ResponseEntity<List<Chapter>> readAllChapters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            logger.info("*******************Reading all chapters!*******************");
            Pageable pageable = PageRequest.of(page, size);
            List<Chapter> chapters = service.readAllChapters(pageable);
            return ResponseEntity.status(HttpStatus.OK).body(chapters);
        } catch (RuntimeException e) {
            logger.error("*******************Error to read all chapters!*******************");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
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

    @PutMapping("/edit")
    public ResponseEntity<String> updateChapterById(@RequestParam Long id, @RequestBody UpdateChapterDTO dto) {
        try {
            service.updateChapter(id, dto);
            logger.info("*******************Updating chapter!*******************");
            return ResponseEntity.status(HttpStatus.OK).body("Chapter updated successfully!");
        } catch (RuntimeException e) {
            logger.error("*******************Error to update chapter!*******************");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
