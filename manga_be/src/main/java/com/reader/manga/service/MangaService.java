package com.reader.manga.service;

import com.reader.manga.dto.MangaDTO;
import com.reader.manga.model.Manga;
import com.reader.manga.repository.MangaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MangaService {

    @Autowired
    private MangaRepository repository;

    public MangaDTO createManga(MangaDTO dto) {
        try {
            Manga manga = new Manga(dto.title(), dto.description(), dto.size(), dto.creationDate(), dto.closingDate(), dto.status(), dto.gender(), dto.author(),  dto.image());
            repository.save(manga);
            return dto;
        }catch (Exception e) {
            throw new RuntimeException("Error creating Manga. Please try again...");
        }
    }
}
