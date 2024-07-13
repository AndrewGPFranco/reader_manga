package com.reader.manga.controller;

import com.reader.manga.dto.ChapterDTO;
import com.reader.manga.model.Chapter;
import com.reader.manga.service.ChapterService;
import com.reader.manga.service.MangaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/chapter")
public class ChapterController {

    private static final Logger logger = LoggerFactory.getLogger(MangaService.class);

    @Autowired
    private ChapterService chapterService;

    @PostMapping("chapter")
    public void createChapter(@RequestBody ChapterDTO dto, Long id) {
        chapterService.createChapter(dto);
    }

    @GetMapping("readAll")
    public List<Chapter> readAllChapters() {
        return chapterService.listOfChapters();
    }
}
