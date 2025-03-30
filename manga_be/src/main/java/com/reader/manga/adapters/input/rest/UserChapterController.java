package com.reader.manga.adapters.input.rest;

import com.reader.manga.adapters.input.dtos.chapter.UserChapterDTO;
import com.reader.manga.domain.services.UserChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/chapter")
public class UserChapterController {

    private final UserChapterService userChapterService;

    @PostMapping()
    public ResponseEntity<Object> criaAssociacao(@RequestBody UserChapterDTO dto) {
        try {
            userChapterService.criarAssociacao(dto);
            return ResponseEntity.ok().body("Associação salva com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
