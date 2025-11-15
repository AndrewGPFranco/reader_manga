package com.reader.manga.adapters.input.rest;

import com.reader.manga.domain.entities.users.User;
import com.reader.manga.domain.services.HistoryService;
import com.reader.manga.domain.valueobjects.mangas.HistoryMangaOutputVO;
import com.reader.manga.domain.valueobjects.mangas.HistoryMangaVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/get-history")
    Page<HistoryMangaOutputVO> getHistoryByUser(@AuthenticationPrincipal User user,
                                                @RequestParam("numberPage") Integer numberPage) {
        return historyService.getHistoricoDoUsuario(user, numberPage);
    }

}
