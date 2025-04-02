package com.reader.manga.domain.job.chapter;

import com.reader.manga.domain.exceptions.NotFoundException;
import com.reader.manga.domain.job.base.ColetorBaseUpload;
import com.reader.manga.domain.entities.mangas.Chapter;
import com.reader.manga.domain.entities.mangas.Manga;
import com.reader.manga.domain.entities.mangas.Pagina;
import com.reader.manga.ports.repositories.ChapterRepository;
import com.reader.manga.ports.repositories.MangaRepository;
import com.reader.manga.ports.repositories.PaginaRepository;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobChapter extends ColetorBaseUpload {

    private static final String BASE_PATH = "/app/uploads/";
    private static final String IMAGE_FORMAT = "PNG";
    private static final int BATCH_SIZE = 100;
    private static final int DPI = 300;
    private static final int THREAD_POOL_SIZE = Runtime.getRuntime().availableProcessors();

    private final MangaRepository mangaRepository;
    private final PaginaRepository paginaRepository;
    private final ChapterRepository capituloRepository;

    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    private final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_POOL_SIZE);

    /**
     * Executa o job de processamento do capítulo
     * @param file Arquivo PDF do capítulo
     * @param varargs Argumentos variáveis (nome do mangá, nome do capítulo)
     * @throws IOException se ocorrer erro no processamento do arquivo
     */
    @Override
    public void executa(MultipartFile file, String... varargs) throws IOException {
        String nomeManga = varargs[0];
        String nomeCapitulo = "Cap_" + varargs[1];

        validateInput(file, nomeManga);

        Manga manga = mangaRepository.findByTitle(nomeManga)
                .orElseThrow(() -> {
                    log.error("Mangá com título: {} não encontrado!", nomeManga);
                    return new NotFoundException("Mangá não encontrado!");
                });

        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            int numeroTotalDePaginas = document.getNumberOfPages();

            Chapter capitulo = createChapter(nomeManga, nomeCapitulo, manga, numeroTotalDePaginas);
            String basePath = prepareBasePath(manga.getTitle(), nomeCapitulo);

            List<Pagina> paginas = processPages(pdfRenderer, numeroTotalDePaginas, basePath, capitulo);

            capitulo.setPages(paginas);
            capituloRepository.save(capitulo);

            for (SseEmitter emitter : emitters) {
                emitter.send("Job executado com sucesso!");
                emitter.complete();
            }
            emitters.clear();
        }
    }

    private void validateInput(MultipartFile file, String nomeManga) {
        if (file == null || file.isEmpty()) {
            log.error("Arquivo MultipartFile é nulo ou vazio!");
            throw new IllegalArgumentException("Arquivo não fornecido!");
        }
        if (nomeManga == null || nomeManga.trim().isEmpty()) {
            log.error("Nome do mangá não fornecido!");
            throw new IllegalArgumentException("Nome do mangá é obrigatório!");
        }
    }

    private Chapter createChapter(String nomeManga, String nomeCapitulo, Manga manga, int totalPages) {
        String prefixoCap = Arrays.stream(nomeManga.split(" "))
                .map(s -> String.valueOf(s.charAt(0)).toLowerCase())
                .collect(Collectors.joining());

        Chapter capitulo = new Chapter();
        capitulo.setTitle(prefixoCap + nomeCapitulo);
        capitulo.setManga(manga);
        capitulo.setNumberPages(totalPages);
        return capituloRepository.save(capitulo);
    }

    private String prepareBasePath(String mangaTitle, String nomeCapitulo) {
        String pathBase = BASE_PATH + String.join("", mangaTitle.split(" ")) + nomeCapitulo;
        File directory = new File(pathBase);
        if (!directory.exists() && !directory.mkdirs()) {
            throw new RuntimeException("Falha ao criar diretório: " + pathBase);
        }
        return pathBase;
    }

    private List<Pagina> processPages(PDFRenderer pdfRenderer, int totalPages,
                                      String basePath, Chapter capitulo) throws IOException {
        List<Pagina> paginas = new ArrayList<>();

        for (int i = 0; i < totalPages; i++) {
            final int pageIndex = i;
            BufferedImage image = pdfRenderer.renderImageWithDPI(pageIndex, DPI);
            String outputPath = basePath + "/pagina_" + pageIndex + ".png";

            executorService.submit(() -> {
                try {
                    ImageIO.write(image, IMAGE_FORMAT, new File(outputPath));
                } catch (IOException e) {
                    log.error("Erro ao escrever imagem da página {}: {}", pageIndex, e.getMessage());
                }
            });

            Pagina pagina = new Pagina();
            pagina.setPathPage(outputPath);
            pagina.setChapter(capitulo);
            paginas.add(pagina);

            if (paginas.size() >= BATCH_SIZE || pageIndex == totalPages - 1) {
                paginas.forEach(paginaRepository::save);
                paginas.clear();
            }

            int progresso = (int) ((i / (double) totalPages) * 100);
            for (SseEmitter emitter : emitters) {
                try {
                    emitter.send(progresso);
                } catch (IOException e) {
                    emitter.complete();
                    emitters.remove(emitter);
                }
            }
        }
        return paginas;
    }

    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
    }

    @PreDestroy
    public void destroy() {
        executorService.shutdown();
    }

}