package com.reader.manga.service;

import com.reader.manga.enums.FeedbackType;
import com.reader.manga.enums.StatusType;
import com.reader.manga.model.Comment;
import com.reader.manga.model.Manga;
import com.reader.manga.repository.CommentRepository;
import com.reader.manga.repository.MangaRepository;
import com.reader.manga.utils.AbstractTests;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Date;

class CommentServiceTest extends AbstractTests {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private MangaRepository mangaRepository;

    @Test
    @DisplayName("Responsável por salvar um comentário no banco de dados.")
    void testeAdicionaComentario() {
        Manga manga = new Manga(
                "One Piece",
                "Um mangá sobre piratas",
                1135,
                new Date(),
                null,
                StatusType.ONGOING,
                "Shounen",
                "Eiichiro Oda",
                "www.imagem.com.br"
        );

        mangaRepository.save(manga);

        Comment comment = new Comment(
                "Andrew Silva",
                "Excelente mangá",
                LocalDateTime.now(),
                FeedbackType.LIKED,
                manga
        );

        commentRepository.save(comment);

        Assertions.assertEquals(1, commentRepository.findAll().size());
    }
}