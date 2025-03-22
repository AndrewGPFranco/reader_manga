package com.reader.manga.service;

import com.reader.manga.dto.page.PageDTO;
import com.reader.manga.dto.page.UpdatePageDTO;
import com.reader.manga.exception.CreationErrorException;
import com.reader.manga.exception.NotFoundException;
import com.reader.manga.dto.chapter.ChapterDTO;
import com.reader.manga.dto.chapter.UpdateChapterDTO;
import com.reader.manga.model.Chapter;
import com.reader.manga.model.Manga;
import com.reader.manga.model.Pagina;
import com.reader.manga.repository.ChapterRepository;
import com.reader.manga.repository.MangaRepository;
import com.reader.manga.repository.PaginaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChapterService {

    private final ChapterRepository repository;
    private final MangaRepository mangaRepository;
    private final PaginaRepository paginaRepository;

    public void createChapter(ChapterDTO dto) {
        try {
            Optional<Manga> mangaById = mangaRepository.findById(dto.mangaId());
            if (mangaById.isPresent()) {
                Chapter chapter = new Chapter(dto.title(), 0, mangaById.get());
                repository.save(chapter);
                log.info("Capítulo salvo.");
            } else {
                log.error("manga com id {} não encontrado", dto.mangaId());
                throw new NotFoundException("manga com id " + dto.mangaId() + " não encontrado");
            }
        } catch (Exception e) {
            log.error("Erro ao salvar capítulo. Por favor, tente novamente...");
            throw new CreationErrorException("Erro ao salvar capítulo. Por favor, tente novamente... " + e.getMessage());
        }
    }

    public Page<Chapter> readAllChapters(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public void deleteChapter(Long id) {
        repository.findById(id);
        repository.deleteById(id);
    }

    public void updateChapter(Long id, UpdateChapterDTO dto) {
        Chapter chapter = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));

        UtilsService.updateField(dto.title(), chapter::setTitle);

        repository.save(chapter);
    }

    public Chapter getChapterByID(Long id) {
        Supplier<NotFoundException> exceptionSupplier = () -> new NotFoundException("Chapter not found");
        return repository.findById(id).orElseThrow(exceptionSupplier);
    }

    public void pageChapterRegister(PageDTO pageDTO) {
        Chapter chapter = repository.findById(pageDTO.chapter_id()).orElseThrow(() -> new NotFoundException("Chapter not found."));
        Pagina page = new Pagina(pageDTO.page(), chapter);
        chapter.setNumberPages(chapter.getNumberPages() + 1);
        repository.save(chapter);
        paginaRepository.save(page);
    }

    public Page<Pagina> getAllPages(Pageable pageable) {
        return paginaRepository.findAll(pageable);
    }

    public void deletePage(Long idPage) {
        Pagina pagina = paginaRepository.findById(idPage).orElseThrow(() -> new NotFoundException("Pagina não encontrada."));
        Chapter chapter = repository.findById(pagina.getChapter().getId()).orElseThrow(() -> new NotFoundException("Capítulo não encontrado."));

        boolean removed = chapter.getPages().removeIf(page -> page.getId().equals(idPage));

        if (!removed)
            throw new NotFoundException("Pagina não encontrada.");

        if (chapter.getNumberPages() > 0)
            chapter.setNumberPages(chapter.getNumberPages() - 1);

        repository.save(chapter);
    }

    public void updatePage(Long id, UpdatePageDTO dto) {
        Pagina page = paginaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Page not found"));

        UtilsService.updateField(dto.page(), page::setPathPage);

        paginaRepository.save(page);
    }


    public List<Pagina> getCapituloPorId(Long idChapter) {
        return paginaRepository.findByIdChapter(idChapter);
    }
}
