package com.reader.manga.service;

import com.reader.manga.dto.manga.GetMangaDTO;
import com.reader.manga.dto.manga.MangaDTO;
import com.reader.manga.dto.manga.UpdateMangaDTO;
import com.reader.manga.exception.CreationErrorException;
import com.reader.manga.exception.NotFoundException;
import com.reader.manga.model.Manga;
import com.reader.manga.repository.MangaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MangaService {

    private final MangaRepository repository;

    public MangaService(MangaRepository repository) {
        this.repository = repository;
    }

    public GetMangaDTO createManga(MangaDTO dto) {
        try {
            Manga manga = new Manga(dto.title(), dto.description(), dto.size(), dto.creationDate(), dto.closingDate(), dto.status(), dto.gender(), dto.author(),  dto.image());
            Manga savedManga = repository.save(manga);
            return new GetMangaDTO(savedManga.getId(), savedManga.getTitle(), savedManga.getDescription(), savedManga.getSize(), savedManga.getCreationDate(), savedManga.getClosingDate(), savedManga.getStatus(), savedManga.getGender(), savedManga.getAuthor(),  savedManga.getImage());
        }catch (Exception e) {
            throw new CreationErrorException("Error creating Manga. Please try again... " + e.getMessage());
        }
    }

    public void deleteManga(Long id) {
        repository.findById(id);
        repository.deleteById(id);
    }

    public List<Manga> readAllMangas(Pageable pageable) {
        if (pageable == null) {
            return repository.findAll();
        }

        Page<Manga> pageResult = repository.findAll(pageable);
        return pageResult.getContent();
    }

    public void updateManga(Long id, UpdateMangaDTO dto) {
        Manga manga = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Manga not found"));

        UtilsService.updateField(dto.title(), manga::setTitle);
        UtilsService.updateField(dto.description(), manga::setDescription);
        UtilsService.updateField(dto.size(), manga::setSize);
        UtilsService.updateField(dto.creationDate(), manga::setCreationDate);
        UtilsService.updateField(dto.closingDate(), manga::setClosingDate);
        UtilsService.updateField(dto.status(), manga::setStatus);
        UtilsService.updateField(dto.author(), manga::setAuthor);
        UtilsService.updateField(dto.gender(), manga::setGender);
        UtilsService.updateField(dto.image(), manga::setImage);

        repository.save(manga);
    }

    public List<Manga> getAll() {
        return repository.findAll();
    }

    public Manga findById(Long id) {
        return repository.findById(id).orElseThrow(() -> new NotFoundException("Mangá not found"));
    }

}