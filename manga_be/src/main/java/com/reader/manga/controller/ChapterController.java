package com.reader.manga.controller;

import com.reader.manga.dto.chapter.ChapterDTO;
import com.reader.manga.dto.chapter.GetChapterDTO;
import com.reader.manga.dto.chapter.UpdateChapterDTO;
import com.reader.manga.exception.CreationErrorException;
import com.reader.manga.model.Chapter;
import com.reader.manga.service.ChapterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/chapter")
public class ChapterController {

    private static final Logger logger = LoggerFactory.getLogger(ChapterController.class);

    private final ChapterService service;

    public ChapterController(ChapterService service) {
        this.service = service;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> createChapter(@RequestBody ChapterDTO dto) {
        try {
            logger.info("*******************Creating mangá!*******************");
            service.createChapter(dto);
            GetChapterDTO chapterDTO = new GetChapterDTO(dto.title(), dto.description(), dto.numberPages());
            return ResponseEntity.status(HttpStatus.CREATED).body(chapterDTO);
        } catch(CreationErrorException e) {
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteChapter(@PathVariable Long id) {
        try {
            logger.info("*******************Deleting mangá!*******************");
            service.deleteChapter(id);
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Object> getChapterById(@PathVariable Long id) {
        try {
            Optional<Chapter> chapter = service.getChapterByID(id);
            logger.info("*******************Searching chapter!*******************");
            return ResponseEntity.ok().body(chapter);
        } catch (RuntimeException e) {
            logger.error("*******************Error when searching for chapter!*******************");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
