package com.reader.manga.controller;

import com.reader.manga.dto.chapter.*;
import com.reader.manga.dto.page.PageDTO;
import com.reader.manga.dto.page.UpdatePageDTO;
import com.reader.manga.model.Chapter;
import com.reader.manga.service.ChapterService;
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
@RequestMapping("/api/v1/chapter")
public class ChapterController {

    private static final Logger logger = LoggerFactory.getLogger(ChapterController.class);

    private final ChapterService service;

    public ChapterController(ChapterService service) {
        this.service = service;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Object> createChapter(@RequestBody ChapterDTO dto) {
        logger.info("*******************Creating mangá!*******************");
        service.createChapter(dto);
        GetChapterDTO chapterDTO = new GetChapterDTO(dto.title(), dto.description(), 0);
        return ResponseEntity.status(HttpStatus.CREATED).body(chapterDTO);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/readAll")
    public ResponseEntity<List<Chapter>> readAllChapters(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("*******************Reading all chapters!*******************");
        Pageable pageable = PageRequest.of(page, size);
        List<Chapter> chapters = service.readAllChapters(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(chapters);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteChapter(@PathVariable Long id) {
        logger.info("*******************Deleting mangá!*******************");
        service.deleteChapter(id);
        return ResponseEntity.status(HttpStatus.OK).body("Chapter deleted successfully!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/edit/{id}")
    public ResponseEntity<String> updateChapterById(@PathVariable Long id, @RequestBody UpdateChapterDTO dto) {
        service.updateChapter(id, dto);
        logger.info("*******************Updating chapter!*******************");
        return ResponseEntity.status(HttpStatus.OK).body("Chapter updated successfully!");
    }

    @GetMapping("/read/{id}")
    public ResponseEntity<Object> getChapterById(@PathVariable Long id) {
        Chapter chapter = service.getChapterByID(id);
        logger.info("*******************Searching chapter!*******************");
        return ResponseEntity.ok().body(chapter);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register/page")
    public ResponseEntity<String> registerPageChapter(@RequestBody @Valid PageDTO pageDTO) {
        service.pageChapterRegister(pageDTO);
        logger.info("Chapter page registered successfully");
        return ResponseEntity.ok().body("Chapter page registered successfully");
    }

    @GetMapping("/getAll-pages")
    public ResponseEntity<List<com.reader.manga.model.Page>> getAllPages() {
        List<com.reader.manga.model.Page> allPages = service.getAllPages();
        logger.info("Searching all pages");
        return ResponseEntity.ok().body(allPages);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/page/{idPage}/{idChapter}")
    public ResponseEntity<String> deletePage(@PathVariable Long idPage, @PathVariable Long idChapter) {
        logger.info("*******************Deleting page!*******************");
        service.deletePage(idPage,idChapter);
        return ResponseEntity.status(HttpStatus.OK).body("Page deleted successfully!");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/edit/page/{id}")
    public ResponseEntity<String> updatePageById(@PathVariable Long id, @RequestBody UpdatePageDTO dto) {
        service.updatePage(id, dto);
        logger.info("*******************Updating page!*******************");
        return ResponseEntity.status(HttpStatus.OK).body("Page updated successfully!");
    }

}
