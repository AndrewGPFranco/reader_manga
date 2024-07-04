package com.reader.manga.controller;

import com.reader.manga.dto.GetMangaDTO;
import com.reader.manga.dto.MangaDTO;
import com.reader.manga.service.MangaService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/manga")
public class MangaController {

    @Autowired
    private MangaService service;

    private static final Logger logger = LoggerFactory.getLogger(MangaService.class);

    @PostMapping("/create")
    public GetMangaDTO createManga(@RequestBody @Valid MangaDTO dto) {
        logger.info("Creating mangá!");
        return service.createManga(dto);
    }
}
