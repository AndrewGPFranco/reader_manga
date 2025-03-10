package com.reader.manga.job.chapter;

import com.reader.manga.exception.NotFoundException;
import com.reader.manga.job.base.ColetorBaseUpload;
import com.reader.manga.model.Chapter;
import com.reader.manga.model.Manga;
import com.reader.manga.model.Pagina;
import com.reader.manga.repository.ChapterRepository;
import com.reader.manga.repository.MangaRepository;
import com.reader.manga.repository.PaginaRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Slf4j
@Component
@RequiredArgsConstructor
public class JobChapter extends ColetorBaseUpload {

    private final MangaRepository mangaRepository;
    private final PaginaRepository paginaRepository;
    private final ChapterRepository capituloRepository;
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void executa(MultipartFile file, String... varargs) throws IOException {

        String nomeManga = varargs[0];
        String nomeCapitulo = varargs[1];

        if (file == null || file.isEmpty()) {
            log.error("Arquivo MultipartFile é nulo ou vazio!");
            throw new IllegalArgumentException("Arquivo não fornecido!");
        }

        Optional<Manga> mangaOptional = mangaRepository.findByTitle(nomeManga);
        if (mangaOptional.isEmpty()) {
            log.error("Mangá com título: {} não encontrado!", nomeManga);
            throw new NotFoundException("Mangá não encontrado!");
        }

        Manga manga = mangaOptional.get();

        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);
            int numeroTotalDePaginas = document.getNumberOfPages();

            StringBuilder prefixoCap = new StringBuilder();
            String[] nomeCapituloSeparado = nomeManga.split(" ");

            for (int i = 0; i <= nomeCapituloSeparado.length - 1; i++) {
                char primeiroCaractere = nomeCapituloSeparado[i].charAt(0);
                prefixoCap.append(String.valueOf(primeiroCaractere).toLowerCase());
            }

            Chapter capitulo = new Chapter();
            capitulo.setTitle(prefixoCap + nomeCapitulo);
            capitulo.setManga(manga);
            capitulo.setNumberPages(numeroTotalDePaginas);
            capituloRepository.save(capitulo);

            String[] s = manga.getTitle().split(" ");
            int quantidadeNomes = s.length - 1;
            StringBuilder pathBase = new StringBuilder();

            for (int i = 0; i <= quantidadeNomes; i++) {
                pathBase.append(s[i]);
            }

            List<Pagina> paginas = new ArrayList<>();
            log.info("Iniciando gravação de {} páginas...", numeroTotalDePaginas);
            for (int i = 0; i < numeroTotalDePaginas; i++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(i, 300);

                int progresso = (int) ((i / (double) numeroTotalDePaginas) * 100);
                log.info("Progresso atual: {}", progresso);
                for (SseEmitter emitter : emitters) {
                    try {
                        emitter.send(progresso);
                    } catch (IOException e) {
                        emitter.complete();
                        emitters.remove(emitter);
                    }
                }

                String outputPath = "/app/uploads/" + pathBase + nomeCapitulo + "/pagina_" + i + ".png";
                File outputFile = new File(outputPath);

                outputFile.getParentFile().mkdirs();
                ImageIO.write(image, "PNG", outputFile);

                Pagina pagina = new Pagina();
                pagina.setPathPage(outputFile.getAbsolutePath());
                pagina.setChapter(capitulo);
                paginaRepository.save(pagina);
                log.info("Página {} salva com sucesso!", i);
                paginas.add(pagina);
            }

            capitulo.setPages(paginas);
            capituloRepository.save(capitulo);
            log.info("Cápitulo {} salvo com sucesso!", nomeCapitulo);
            for (SseEmitter emitter : emitters) {
                emitter.send("Job executado com sucesso!");
                emitter.complete();
            }
            emitters.clear();
        }
    }

    public void addEmitter(SseEmitter emitter) {
        emitters.add(emitter);
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
    }

}
