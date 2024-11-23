package com.reader.manga.service;

import com.reader.manga.dto.manga.MangaDTO;
import com.reader.manga.enums.StatusType;
import com.reader.manga.model.Manga;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(MangaService.class)
class MangaServiceTest {

    @Autowired
    private MangaService service;

    private static final Date DATE = new Date(2024, Calendar.JUNE, 28);

    @Test
    void testCreateMangaWithSuccess() {
        createManga();
        Manga expected = new Manga(1L, "test", "test description", 1,
                DATE,null, StatusType.ONGOING,
                "Andrew","Shonen","url.com.br", true, List.of());

        Manga result = service.findById(expected.getId());

        assertEquals(expected, result);
    }

    private void createManga() {
        MangaDTO mangaDTO = new MangaDTO(
                "test",
                "test description",
                1,
                DATE,
                null,
                StatusType.ONGOING,
                "Andrew",
                "Shonen",
                "url.com.br"
        );

        service.createManga(mangaDTO);
    }

    @Test
    void testDeleteManga() {
        createManga();
        service.deleteManga(1L);
    }
}