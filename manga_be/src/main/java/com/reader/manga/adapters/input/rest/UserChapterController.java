package com.reader.manga.adapters.input.rest;

import com.reader.manga.adapters.input.dtos.chapter.UserChapterDTO;
import com.reader.manga.domain.entities.users.User;
import com.reader.manga.domain.services.UserChapterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/chapter")
public class UserChapterController {

    private final UserChapterService userChapterService;

    @PutMapping()
    public ResponseEntity<Object> criaAssociacao(@RequestBody UserChapterDTO dto) {
        try {
            userChapterService.criarAssociacao(dto);
            return ResponseEntity.ok().body("Associação salva com sucesso!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{idChapter}")
    public ResponseEntity<String> deleteAssociacao(@PathVariable Long idChapter, @AuthenticationPrincipal User user) {
        try {
            userChapterService.deleteAssociacao(idChapter, user.getId());
            return ResponseEntity.ok().body("Associação deletada.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
