package com.reader.manga.service;

import com.reader.manga.dto.chapter.ChapterDTO;
import com.reader.manga.enums.StatusType;
import com.reader.manga.exception.CreationErrorException;
import com.reader.manga.model.Chapter;
import com.reader.manga.model.Manga;
import com.reader.manga.repository.ChapterRepository;
import com.reader.manga.repository.MangaRepository;
import com.reader.manga.repository.PaginaRepository;
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
    private ChapterRepository chapterRepository;

    @Mock
    private MangaRepository mangaRepository;

    @Mock
    private PaginaRepository paginaRepository;

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
                .description("Primeiro capítulo de One Piece.")
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
        Mockito.when(mangaRepository.findById(1L)).thenReturn(Optional.of(manga));

        Assertions.assertDoesNotThrow(() -> spyService.createChapter(getChapterDto()));

        Mockito.verify(chapterRepository, Mockito.times(1)).save(Mockito.any(Chapter.class));
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