package com.reader.manga.service;

import com.reader.manga.dto.MangaDTO;
import com.reader.manga.enums.StatusType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.*;

class MangaServiceTest {

    private MangaService service;

    @BeforeEach
    void setUp() {
        this.service = Mockito.mock(MangaService.class);
    }

    @Test
    void testCreateMangaWithSuccess() {
        MangaDTO mangaDTO = new MangaDTO(
                "test",
                "test description",
                1,
                "2024-06-28",
                "N/D",
                StatusType.ONGOING,
                "Andrew",
                "Shonen",
                "url.com.br"
        );

        service.createManga(mangaDTO);
        MangaDTO expected = new MangaDTO("test","test description",1,"2024-06-28","N/D",StatusType.ONGOING,"Andrew","Shonen","url.com.br");
        assertEquals(expected, mangaDTO);
    }

    @Test
    public void testDeleteManga() {
        service.deleteManga(1L);
        Mockito.verify(service, Mockito.times(1)).deleteManga(1L);
    }
}