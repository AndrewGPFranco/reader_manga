package com.reader.manga.domain.services;

import com.reader.manga.adapters.input.dtos.chapter.GetChapterDTO;
import com.reader.manga.adapters.input.dtos.page.PageDTO;
import com.reader.manga.adapters.input.dtos.page.UpdatePageDTO;
import com.reader.manga.domain.entities.users.User;
import com.reader.manga.domain.entities.users.UserChapter;
import com.reader.manga.domain.enums.StatusType;
import com.reader.manga.domain.exceptions.CreationErrorException;
import com.reader.manga.domain.exceptions.NotFoundException;
import com.reader.manga.adapters.input.dtos.chapter.ChapterDTO;
import com.reader.manga.adapters.input.dtos.chapter.UpdateChapterDTO;
import com.reader.manga.domain.entities.mangas.Chapter;
import com.reader.manga.domain.entities.mangas.Manga;
import com.reader.manga.domain.entities.mangas.Pagina;
import com.reader.manga.ports.repositories.ChapterRepository;
import com.reader.manga.ports.repositories.MangaRepository;
import com.reader.manga.ports.repositories.PaginaRepository;
import com.reader.manga.ports.repositories.UserChapterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChapterService {

    private final MangaRepository mangaRepository;
    private final PaginaRepository paginaRepository;
    private final UserMangaService userMangaService;
    private final ChapterRepository chapterRepository;
    private final UserChapterRepository userChapterRepository;

    public void createChapter(ChapterDTO dto) {
        try {
            Optional<Manga> mangaById = mangaRepository.findById(dto.mangaId());
            if (mangaById.isPresent()) {
                Chapter chapter = new Chapter(dto.title(), 0, mangaById.get());
                chapterRepository.save(chapter);
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
        return chapterRepository.findAll(pageable);
    }

    public void deleteChapter(Long id) {
        chapterRepository.findById(id);
        chapterRepository.deleteById(id);
    }

    public void updateChapter(Long id, UpdateChapterDTO dto) {
        Chapter chapter = chapterRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chapter not found"));

        UtilsService.updateField(dto.title(), chapter::setTitle);
        UtilsService.updateField(dto.status(), chapter::setStatus);
        UtilsService.updateField(dto.readingProgress(), chapter::setReadingProgress);

        chapterRepository.save(chapter);
    }

    public Chapter getChapterByID(Long id) {
        Supplier<NotFoundException> exceptionSupplier = () -> new NotFoundException("Chapter not found");
        return chapterRepository.findById(id).orElseThrow(exceptionSupplier);
    }

    public void pageChapterRegister(PageDTO pageDTO) {
        Chapter chapter = chapterRepository.findById(pageDTO.chapter_id()).orElseThrow(() -> new NotFoundException("Chapter not found."));
        Pagina page = new Pagina(pageDTO.page(), chapter);
        chapter.setNumberPages(chapter.getNumberPages() + 1);
        chapterRepository.save(chapter);
        paginaRepository.save(page);
    }

    public Page<Pagina> getAllPages(Pageable pageable) {
        return paginaRepository.findAll(pageable);
    }

    public void deletePage(Long idPage) {
        Pagina pagina = paginaRepository.findById(idPage).orElseThrow(() -> new NotFoundException("Pagina não encontrada."));
        Chapter chapter = chapterRepository.findById(pagina.getChapter().getId()).orElseThrow(() -> new NotFoundException("Capítulo não encontrado."));

        boolean removed = chapter.getPages().removeIf(page -> page.getId().equals(idPage));

        if (!removed)
            throw new NotFoundException("Pagina não encontrada.");

        if (chapter.getNumberPages() > 0)
            chapter.setNumberPages(chapter.getNumberPages() - 1);

        chapterRepository.save(chapter);
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

    /**
     * A idéia é que o progresso só seja atualizado caso seja maior do que esta no banco.
     */
    public void updateReadingProgress(UpdateChapterDTO dto) {
        User user = userMangaService.getUserById(dto.idUser());
        Chapter chapter = getChapterByID(dto.idChapter());
        UserChapter dadoJaSalvo = userChapterRepository.findByIdChapterAndUser(dto.idChapter(), dto.idUser());

        if(dadoJaSalvo != null) {
            if(dto.readingProgress() > dadoJaSalvo.getProgress()) {
                dadoJaSalvo.setProgress(dto.readingProgress());
                userChapterRepository.save(dadoJaSalvo);
            }
        } else {
            UserChapter userChapter = UserChapter.builder()
                    .user_id(user)
                    .chapter_id(chapter)
                    .progress(dto.readingProgress())
                    .build();
            userChapterRepository.save(userChapter);
        }
    }

    public Integer getReadingProgress(Long idChapter) {
        Chapter chapter = getChapterByID(idChapter);

        return chapter.getReadingProgress();
    }

    public List<GetChapterDTO> getAllReadingProgressPageable(Pageable pageable, Long idUser) {
        Page<UserChapter> allReadingsInProgress = userChapterRepository.findAllReadingsInProgress(pageable, idUser);

        List<GetChapterDTO> allChapter = new ArrayList<>(allReadingsInProgress.getSize());

        allReadingsInProgress.forEach(chapter -> {
            Optional<Chapter> chapterId = chapterRepository.findById(chapter.getChapter_id().getId());
            if(chapterId.isPresent()) {
                Optional<Manga> manga = mangaRepository.findById(chapterId.get().getManga().getId());
                if(manga.isPresent()) {
                    String urlImage = manga.get().getImage();
                    String nameManga = manga.get().getTitle();

                    GetChapterDTO chapterDTO = new GetChapterDTO(chapterId.get().getId(), chapterId.get().getTitle(),
                            chapterId.get().getNumberPages(), chapter.getStatus(),
                            chapter.getProgress(), urlImage, nameManga,
                            allReadingsInProgress.getTotalPages());
                    allChapter.add(chapterDTO);
                }
            }
        });

        removeFinishedChapters(allChapter);

        return allChapter;
    }

    private void removeFinishedChapters(List<GetChapterDTO> allChapter) {
        allChapter.removeIf(chapter -> chapter.status().equals(StatusType.FINISHED));
    }
}
