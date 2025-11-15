package com.reader.manga.domain.services;

import com.reader.manga.domain.entities.mangas.Chapter;
import com.reader.manga.domain.entities.mangas.History;
import com.reader.manga.domain.entities.users.User;
import com.reader.manga.domain.enums.StatusType;
import com.reader.manga.domain.exceptions.NotFoundException;
import com.reader.manga.domain.valueobjects.mangas.HistoryMangaVO;
import com.reader.manga.ports.repositories.ChapterRepository;
import com.reader.manga.ports.repositories.HistoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final ChapterRepository chapterRepository;

    public void preencheHistorico(User user, HistoryMangaVO historyMangaVO) {
        Chapter chapter = getChapter(historyMangaVO);

        History history = historyRepository.getHistory(user.getId(), historyMangaVO.idChapter(), chapter.getManga().getId());

        if (history == null)
            inicializaMarcacaoHistorico(user, historyMangaVO, chapter);
        else
            atualizaMarcacaoHistorico(user, historyMangaVO, history, chapter);
    }

    private Chapter getChapter(HistoryMangaVO historyMangaVO) {
        return chapterRepository.findById(historyMangaVO.idChapter())
                .orElseThrow(() -> new NotFoundException("Nenhum capítulo encontrado com o ID: " + historyMangaVO.idChapter()));
    }

    public void inicializaMarcacaoHistorico(User user, HistoryMangaVO historyMangaVO, Chapter chapter) {
        History history = new History(user.getId(), historyMangaVO.idChapter(), chapter.getManga().getId(), StatusType.ONGOING, getDataHoraAtual());

        historyRepository.save(history);
        log.info("Marcação no tempo iniciada para: Usuário: {} | Id Manga: {} | Id Capítulo:" +
                " {}", user.getId(), chapter.getManga().getId(), history.getIdCapitulo());
    }

    private @NotNull OffsetDateTime getDataHoraAtual() {
        return LocalDateTime.now().atZone(ZoneId.of("America/Sao_Paulo")).toOffsetDateTime();
    }

    private void atualizaMarcacaoHistorico(User user, HistoryMangaVO historyMangaVO, History history, Chapter chapter) {
        Integer ultimaPagina = historyMangaVO.currentProgress();

        history.setLastCheck(getDataHoraAtual());

        if (chapter.getNumberPages().equals(ultimaPagina)) {
            history.setStatusType(StatusType.FINISHED);
            historyRepository.save(history);
            log.info("Marcação de tempo finalizada para: Usuário: {} Id Manga: {} Id Capítulo: {}", user.getId(),
                    chapter.getManga().getId(), history.getIdCapitulo());
        } else historyRepository.save(history);
    }

}
