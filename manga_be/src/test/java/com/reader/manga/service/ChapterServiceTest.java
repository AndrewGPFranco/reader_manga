package com.reader.manga.service;

import com.reader.manga.adapters.input.dtos.chapter.ChapterDTO;
import com.reader.manga.domain.services.ChapterService;
import com.reader.manga.domain.enums.StatusType;
import com.reader.manga.domain.exceptions.CreationErrorException;
import com.reader.manga.domain.entities.mangas.Chapter;
import com.reader.manga.domain.entities.mangas.Manga;
import com.reader.manga.ports.repositories.ChapterRepository;
import com.reader.manga.ports.repositories.MangaRepository;
import com.reader.manga.ports.repositories.PaginaRepository;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.util.Date;
import java.util.List;
import java.util.Optional;

class ChapterServiceTest {

    @Spy
    @InjectMocks
    private ChapterService spyService;

    @Mock
    private ChapterRepository jpaChapterRepository;

    @Mock
    private MangaRepository jpaMangaRepository;

    @Mock
    private PaginaRepository jpaPaginaRepository;

    private static AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterAll
    static void tearDown() throws Exception {
        if (closeable != null) {
            closeable.close();
        }
    }

    private ChapterDTO getChapterDto() {
        return ChapterDTO.builder()
                .title("Capítulo 1")
                .mangaId(1L)
                .build();
    }

    private Manga getEntityManga() {
        return Manga.builder()
                .id(1L)
                .size(1135)
                .gender("Shounen")
                .title("One Piece")
                .closingDate(null)
                .chapters(List.of())
                .comments(List.of())
                .author("Eiichiro Oda")
                .creationDate(new Date())
                .status(StatusType.ONGOING)
                .description("Mangá sobre piratas.")
                .image("www.onepiece.com.br/imagemdecapa.png")
                .build();
    }

    @Test
    @DisplayName("Deve criar um capítulo com sucesso.")
    void testeCreateChapter() {
        Manga manga = getEntityManga();
        Mockito.when(jpaMangaRepository.findById(1L)).thenReturn(Optional.of(manga));

        Assertions.assertDoesNotThrow(() -> spyService.createChapter(getChapterDto()));

        Mockito.verify(jpaChapterRepository, Mockito.times(1)).save(Mockito.any(Chapter.class));
    }

    private void chamadaDoMetodoDeCriacaoELancaException() throws CreationErrorException {
        spyService.createChapter(getChapterDto());
    }

    @Test
    @DisplayName("Lança uma exception por não encontrar nenhum mangá com o id informado.")
    void testeCreateChapterComErro() {
        CreationErrorException exception = Assertions
                .assertThrows(CreationErrorException.class, this::chamadaDoMetodoDeCriacaoELancaException);

        String expected = "Erro ao salvar capítulo. Por favor, tente novamente...";
        String result = exception.getMessage();

        Assertions.assertTrue(result.contains(expected));
    }

}