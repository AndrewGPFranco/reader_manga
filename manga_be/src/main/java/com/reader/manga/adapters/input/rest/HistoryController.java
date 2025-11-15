package com.reader.manga.adapters.input.rest;

import com.reader.manga.domain.entities.users.User;
import com.reader.manga.domain.services.HistoryService;
import com.reader.manga.domain.valueobjects.mangas.HistoryMangaVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/history")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryService historyService;

    @PostMapping("/preenche-historico")
    void inicializaHistorico(@RequestBody @Valid HistoryMangaVO historyMangaVO,
                             @AuthenticationPrincipal User user) {
        historyService.preencheHistorico(user, historyMangaVO);
    }

}
