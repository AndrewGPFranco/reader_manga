package com.reader.manga.domain.job.chapter;

import com.reader.manga.domain.exceptions.NotFoundException;
import com.reader.manga.domain.job.base.ColetorBaseUpload;
import com.reader.manga.domain.entities.mangas.Chapter;
import com.reader.manga.domain.entities.mangas.Manga;
import com.reader.manga.domain.entities.mangas.Pagina;
import com.reader.manga.ports.repositories.ChapterRepository;
import com.reader.manga.ports.repositories.MangaRepository;
import com.reader.manga.ports.repositories.PaginaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

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
     *
     * @param file    Arquivo PDF do capítulo
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

        if (file.getOriginalFilename() != null && file.getOriginalFilename().contains(".zip")) {
            processPagesZip(nomeManga, nomeCapitulo, manga, file);
        } else {
            try (PDDocument document = PDDocument.load(file.getInputStream())) {
                PDFRenderer pdfRenderer = new PDFRenderer(document);
                int numeroTotalDePaginas = document.getNumberOfPages();

                Chapter capitulo = createChapter(nomeManga, nomeCapitulo, manga, numeroTotalDePaginas);
                String basePath = prepareBasePath(manga.getTitle(), nomeCapitulo);

                List<Pagina> paginas = processPages(pdfRenderer, numeroTotalDePaginas, basePath, capitulo);

                capitulo.setPages(paginas);
                capituloRepository.save(capitulo);
            }
        }

        for (SseEmitter emitter : emitters) {
            emitter.send("Job executado com sucesso!");
            emitter.complete();
        }
        emitters.clear();
    }

    private void processPagesZip(String nomeManga, String nomeCapitulo, Manga manga, MultipartFile file) throws IOException {
        Chapter capitulo = createChapter(nomeManga, nomeCapitulo, manga, 1);
        String basePath = BASE_PATH + String.join("", nomeManga.split(" ")) + nomeCapitulo;
        unZip(file, capitulo, basePath);
    }

    public void unZip(MultipartFile zipFile, Chapter capitulo, String basePath) throws IOException {
        File diretorioDestino = new File(basePath);
        if (!diretorioDestino.exists()) {
            boolean isCreated = diretorioDestino.mkdirs();
            throwErrorIfUnableToCreateFileOrDirectory(isCreated);
        }

        List<Pagina> paginas = new ArrayList<>();
        List<File> arquivosExtraidos = preparaArquivosDoZip(zipFile, basePath, diretorioDestino);

        int totalPages = arquivosExtraidos.size();
        capitulo.setNumberPages(totalPages);

        ordenaListaPaginas(arquivosExtraidos);

        List<Pagina> paginasASalvar = new ArrayList<>(totalPages);
        for (int i = 0; i < totalPages; i++) {
            File file = arquivosExtraidos.get(i);
            int index = i + 1;

            String outputPath = basePath + "/pagina_" + index + ".png";

            BufferedImage image = ImageIO.read(file);

            escreveImagem(image, outputPath, i);

            Pagina pagina = new Pagina();
            pagina.setPathPage(outputPath);
            pagina.setChapter(capitulo);
            paginas.add(pagina);
            paginasASalvar.add(pagina);

            if (paginas.size() >= BATCH_SIZE || i == totalPages - 1) {
                paginas.forEach(paginaRepository::save);
                paginas.clear();
            }

            int progresso = (int) ((i / (double) totalPages) * 100);
            for (Iterator<SseEmitter> it = emitters.iterator(); it.hasNext();) {
                SseEmitter emitter = it.next();
                try {
                    emitter.send(progresso);
                } catch (IOException e) {
                    emitter.complete();
                    it.remove();
                }
            }
        }

        capitulo.setPages(paginasASalvar);
        capituloRepository.save(capitulo);
    }

    private void ordenaListaPaginas(List<File> arquivosExtraidos) {
        arquivosExtraidos.sort(Comparator.comparing(a -> {
            try {
                return a.getCanonicalPath();
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }));
    }

    private @NotNull List<File> preparaArquivosDoZip(MultipartFile zipFile, String basePath, File diretorioDestino) throws IOException {
        List<File> arquivosExtraidos = new ArrayList<>();

        try (ZipInputStream zis = new ZipInputStream(zipFile.getInputStream())) {
            ZipEntry entry;

            while ((entry = zis.getNextEntry()) != null) {
                File newFile = new File(basePath, entry.getName());

                String caminhoCanonico = diretorioDestino.getCanonicalPath();
                String novoCaminho = newFile.getCanonicalPath();
                if (!novoCaminho.startsWith(caminhoCanonico)) {
                    throw new IOException("Entrada maliciosa detectada: " + entry.getName());
                }

                if (entry.isDirectory()) {
                    boolean isCreated = newFile.mkdirs();
                    throwErrorIfUnableToCreateFileOrDirectory(isCreated);
                } else {
                    boolean isCreated = new File(newFile.getParent()).mkdirs();
                    throwErrorIfUnableToCreateFileOrDirectory(isCreated);

                    try (FileOutputStream fos = new FileOutputStream(newFile)) {
                        byte[] buffer = new byte[4096];
                        int len;
                        while ((len = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, len);
                        }
                    }

                    if(newFile.getCanonicalPath().contains(".png") || newFile.getCanonicalPath().contains(".jpg")) {
                        arquivosExtraidos.add(newFile);
                    }
                }

                zis.closeEntry();
            }
        }
        return arquivosExtraidos;
    }

    private void throwErrorIfUnableToCreateFileOrDirectory(boolean isCreated) {
        if (!isCreated) {
            log.error("Ocorreu um erro na criação do arquivo");
            throw new RuntimeException("Erro ao criar arquivo");
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

        int indexInicial1 = 1;
        for (int pageIndex = 0; pageIndex < totalPages; pageIndex++) {
            BufferedImage image = pdfRenderer.renderImageWithDPI(pageIndex, DPI);
            String outputPath = basePath + "/pagina_" + indexInicial1 + ".png";

            indexInicial1++;

            escreveImagem(image, outputPath, pageIndex);

            Pagina pagina = new Pagina();
            pagina.setPathPage(outputPath);
            pagina.setChapter(capitulo);
            paginas.add(pagina);

            if (paginas.size() >= BATCH_SIZE || pageIndex == totalPages - 1) {
                paginas.forEach(paginaRepository::save);
                paginas.clear();
            }

            int progresso = (int) ((pageIndex / (double) totalPages) * 100);
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

    private void escreveImagem(BufferedImage image, String outputPath, int pageIndex) {
        executorService.submit(() -> {
            try {
                ImageIO.write(image, IMAGE_FORMAT, new File(outputPath));
            } catch (IOException e) {
                log.error("Erro ao escrever imagem da página {}: {}", pageIndex, e.getMessage());
            }
        });
    }

    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
    }

}