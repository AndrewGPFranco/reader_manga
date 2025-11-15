package com.reader.manga.domain.services;

import com.reader.manga.domain.entities.mangas.Chapter;
import com.reader.manga.domain.entities.mangas.History;
import com.reader.manga.domain.entities.users.User;
import com.reader.manga.domain.enums.StatusType;
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
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final ChapterRepository chapterRepository;

    public void preencheHistorico(User user, HistoryMangaVO historyMangaVO) {
        History history = historyRepository.getHistory(user.getId(), historyMangaVO.idChapter(), historyMangaVO.idManga());

        if (history == null)
            inicializaMarcacaoHistorico(user, historyMangaVO);
        else
            atualizaMarcacaoHistorico(user, historyMangaVO, history);
    }

    public void inicializaMarcacaoHistorico(User user, HistoryMangaVO historyMangaVO) {
        History history = new History(user.getId(), historyMangaVO.idChapter(),
                historyMangaVO.idManga(), StatusType.ONGOING, getDataHoraAtual());

        historyRepository.save(history);
        log.info("Marcação no tempo iniciada para: Usuário: {} Id Manga: {} Id Capítulo:" +
                " {}", user.getId(), historyMangaVO.idManga(), history.getIdCapitulo());
    }

    private @NotNull OffsetDateTime getDataHoraAtual() {
        return LocalDateTime.now().atZone(ZoneId.of("America/Sao_Paulo")).toOffsetDateTime();
    }

    private void atualizaMarcacaoHistorico(User user, HistoryMangaVO historyMangaVO, History history) {
        Integer ultimaPagina = historyMangaVO.currentProgress();
        Optional<Chapter> chapter = chapterRepository.findById(historyMangaVO.idChapter());

        history.setLastCheck(getDataHoraAtual());

        if (chapter.isPresent() && chapter.get().getNumberPages().equals(ultimaPagina)) {
            history.setStatusType(StatusType.FINISHED);
            historyRepository.save(history);
            log.info("Marcação de tempo finalizada para: Usuário: {} Id Manga: {} Id Capítulo: {}", user.getId(),
                    historyMangaVO.idManga(), history.getIdCapitulo());
        }

        historyRepository.save(history);
    }

}
