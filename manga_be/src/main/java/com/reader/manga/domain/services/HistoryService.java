package com.reader.manga.domain.services;

import com.reader.manga.domain.entities.mangas.Chapter;
import com.reader.manga.domain.entities.mangas.History;
import com.reader.manga.domain.entities.users.User;
import com.reader.manga.domain.enums.StatusType;
import com.reader.manga.domain.exceptions.NotFoundException;
import com.reader.manga.domain.valueobjects.mangas.HistoryMangaOutputVO;
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
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRepository historyRepository;
    private final ChapterRepository chapterRepository;

    public void preencheHistorico(User user, HistoryMangaVO historyMangaVO) {
        Chapter chapter = getChapter(historyMangaVO);

        List<History> allHistory = historyRepository.getAllHistoryByUserChapterAndManga(user.getId(),
                historyMangaVO.idChapter(), chapter.getManga().getId());

        boolean isFinalized = allHistory.stream().anyMatch(h -> h.getStatusType().equals(StatusType.FINISHED));

        if (!isFinalized) {
            if (allHistory.isEmpty())
                inicializaMarcacaoHistorico(user, historyMangaVO, chapter);
            else
                atualizaMarcacaoHistorico(user, historyMangaVO, allHistory, chapter);
        }
    }

    private Chapter getChapter(HistoryMangaVO historyMangaVO) {
        return chapterRepository.findById(historyMangaVO.idChapter())
                .orElseThrow(() -> new NotFoundException("Nenhum capítulo encontrado com o ID: " + historyMangaVO.idChapter()));
    }

    public void inicializaMarcacaoHistorico(User user, HistoryMangaVO historyMangaVO, Chapter chapter) {
        Integer ultimaPagina = historyMangaVO.currentProgress();

        StatusType statusType = chapter.getNumberPages().equals(ultimaPagina) ? StatusType.FINISHED : StatusType.ONGOING;

        History history = new History(user.getId(), historyMangaVO.idChapter(), chapter.getManga().getId(), statusType,
                getDataHoraOffSetBrasil(null));

        historyRepository.save(history);
        log.info("Marcação no tempo iniciada para: Usuário: {} | Id Manga: {} | Id Capítulo:" +
                " {}", user.getId(), chapter.getManga().getId(), history.getIdCapitulo());
    }

    private @NotNull OffsetDateTime getDataHoraOffSetBrasil(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null)
            return LocalDateTime.now().atZone(ZoneId.of("America/Sao_Paulo")).toOffsetDateTime();

        return offsetDateTime.withOffsetSameInstant(ZoneOffset.of("-03:00"));
    }

    private void atualizaMarcacaoHistorico(User user, HistoryMangaVO historyMangaVO, List<History> historical, Chapter chapter) {
        Integer ultimaPagina = historyMangaVO.currentProgress();

        if (chapter.getNumberPages().equals(ultimaPagina)) {
            for (History history : historical) {
                history.setStatusType(StatusType.FINISHED);
            }

            History newHistory = new History(user.getId(), historyMangaVO.idChapter(),
                    chapter.getManga().getId(), StatusType.FINISHED, getDataHoraOffSetBrasil(null));
            historical.add(newHistory);

            historyRepository.saveAll(historical);

            log.info("Marcação de tempo finalizada para: Usuário: {} | Id Manga: {} | Id Capítulo: {}", user.getId(),
                    chapter.getManga().getId(), historical.get(0).getIdCapitulo());
        } else {
            History newHistory = new History(user.getId(), historyMangaVO.idChapter(),
                    chapter.getManga().getId(), StatusType.ONGOING, getDataHoraOffSetBrasil(null));
            historyRepository.save(newHistory);
        }
    }

    public List<HistoryMangaOutputVO> getHistoricoDoUsuario(User user) {
        List<History> all = historyRepository.findAllByUser(user.getId());

        return all.stream().map(his -> {
            Optional<Chapter> chapter = chapterRepository.findById(his.getIdCapitulo());

            return new HistoryMangaOutputVO(
                    his.getId(),
                    chapter.map(Chapter::getTitle).orElse("Capítulo não encontrado"),
                    chapter.map(c -> c.getManga().getTitle()).orElse("Manga não encontrado"),
                    his.getStatusType(),
                    getDataHoraOffSetBrasil(his.getLastCheck())
            );
        }).toList();
    }
}
