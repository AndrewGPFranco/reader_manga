package com.reader.manga.service;

import com.reader.manga.dto.page.PageDTO;
import com.reader.manga.dto.page.UpdatePageDTO;
import com.reader.manga.exception.CreationErrorException;
import com.reader.manga.exception.NotFoundException;
import com.reader.manga.dto.chapter.ChapterDTO;
import com.reader.manga.dto.chapter.UpdateChapterDTO;
import com.reader.manga.model.Chapter;
import com.reader.manga.model.Manga;
import com.reader.manga.model.PageChapter;
import com.reader.manga.repository.ChapterRepository;
import com.reader.manga.repository.MangaRepository;
import com.reader.manga.repository.PageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class ChapterService {

    private final ChapterRepository repository;

    private final MangaRepository mangaRepository;

    private final PageRepository pageRepository;

    public ChapterService(ChapterRepository repository, MangaRepository mangaRepository, PageRepository pageRepository) {
        this.repository = repository;
        this.mangaRepository = mangaRepository;
        this.pageRepository = pageRepository;
    }

    public void createChapter(ChapterDTO dto) {
        try {
            Optional<Manga> mangaById = mangaRepository.findById(dto.mangaId());
            if (mangaById.isPresent()) {
                Chapter chapter = new Chapter(dto.title(), dto.description(), 0, mangaById.get());
                repository.save(chapter);
            } else {
                throw new NotFoundException("manga with ID " + dto.mangaId() + " not found");
            }
        } catch (Exception e) {
            throw new CreationErrorException("Error creating Chapter. Please try again... " + e.getMessage());
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
        repository.findById(id);
        repository.deleteById(id);
    }

    public void updateChapter(Long id, UpdateChapterDTO dto) {
        Chapter chapter = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));

        UtilsService.updateField(dto.title(), chapter::setTitle);
        UtilsService.updateField(dto.description(), chapter::setDescription);

        repository.save(chapter);
    }

    public Chapter getChapterByID(Long id) {
        Supplier<NotFoundException> exceptionSupplier = () -> new NotFoundException("Chapter not found");
        return repository.findById(id).orElseThrow(exceptionSupplier);
    }

    public void pageChapterRegister(PageDTO pageDTO) {
        Chapter chapter = repository.findById(pageDTO.chapter_id()).orElseThrow(() -> new NotFoundException("Chapter not found."));
        PageChapter page = new PageChapter(pageDTO.page(), chapter, pageDTO.idChapter());
        chapter.setNumberPages(chapter.getNumberPages() + 1);
        repository.save(chapter);
        pageRepository.save(page);
    }

    public List<PageChapter> getAllPages() {
        return pageRepository.findAll();
    }

    public void deletePage(Long idPage, Long idChapter) {
        Chapter chapter = repository.findById(idChapter).orElseThrow(() -> new NotFoundException("Chapter not found."));
        pageRepository.findById(idPage);
        pageRepository.deleteById(idPage);
        chapter.setNumberPages(chapter.getNumberPages() - 1);
        repository.save(chapter);
    }

    public void updatePage(Long id, UpdatePageDTO dto) {
        PageChapter page = pageRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Page not found"));

        UtilsService.updateField(dto.page(), page::setChapterPage);

        pageRepository.save(page);
    }

}
