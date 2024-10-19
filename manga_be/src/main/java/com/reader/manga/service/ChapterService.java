package com.reader.manga.service;

import com.reader.manga.dto.chapter.ChapterDTO;
import com.reader.manga.dto.chapter.UpdateChapterDTO;
import com.reader.manga.model.Chapter;
import com.reader.manga.model.Manga;
import com.reader.manga.repository.ChapterRepository;
import com.reader.manga.repository.MangaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChapterService {

    private final ChapterRepository repository;

    private final MangaRepository mangaRepository;

    public ChapterService(ChapterRepository repository, MangaRepository mangaRepository) {
        this.repository = repository;
        this.mangaRepository = mangaRepository;
    }

    public void createChapter(ChapterDTO dto) {
        try {
            Optional<Manga> mangaById = mangaRepository.findById(dto.mangaId());
            if (mangaById.isPresent()) {
                Chapter chapter = new Chapter(dto.title(), dto.description(), dto.numberPages(), mangaById.get());
                repository.save(chapter);
            } else {
                throw new RuntimeException("Manga not found");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error creating Chapter. Please try again...", e);
        }
    }

    public List<Chapter> readAllChapters(Pageable pageable) {
        if (pageable == null) {
            return repository.findAll();
        }

        Page<Chapter> pageResult = repository.findAll(pageable);
        return pageResult.getContent();
    }

    public void deleteChapter(Long id) {
        repository.findById(id).orElseThrow(() -> new RuntimeException("Chapter not found"));
        repository.deleteById(id);
    }

    public void updateChapter(Long id, UpdateChapterDTO dto) {
        Chapter chapter = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));

        UtilsService.updateField(dto.title(), chapter::setTitle);
        UtilsService.updateField(dto.description(), chapter::setDescription);
        UtilsService.updateField(dto.numberPages(), chapter::setNumberPages);

        repository.save(chapter);
    }

}
