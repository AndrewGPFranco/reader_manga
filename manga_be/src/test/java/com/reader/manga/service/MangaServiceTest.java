package com.reader.manga.service;

import com.reader.manga.dto.manga.MangaDTO;
import com.reader.manga.enums.StatusType;
import com.reader.manga.model.Manga;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(MangaService.class)
class MangaServiceTest {

    @Autowired
    private MangaService service;

    @Test
    void testCreateMangaWithSuccess() {
        Date date = new Date(2024, Calendar.JUNE, 28);
        MangaDTO mangaDTO = new MangaDTO(
                "test",
                "test description",
                1,
                date,
                null,
                StatusType.ONGOING,
                "Andrew",
                "Shonen",
                "url.com.br"
        );

        service.createManga(mangaDTO);
        Manga expected = new Manga(1L, "test", "test description", 1,
                date,null, StatusType.ONGOING,
                "Andrew","Shonen","url.com.br", null);

        Manga result = service.findById(expected.getId());

        assertEquals(expected, result);
    }

    @Test
    public void testDeleteManga() {
        service.deleteManga(1L);
        Mockito.verify(service, Mockito.times(1)).deleteManga(1L);
    }
}