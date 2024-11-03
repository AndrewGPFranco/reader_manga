package com.reader.manga.service;

import com.reader.manga.repository.ChapterRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ChapterServiceTest {

    @Autowired
    private ChapterService service;

    @Autowired
    private ChapterRepository repository;

    @Test
    @DisplayName("Teste responsável por criar um capítulo")
    void testCreateChapter() {

    }

}