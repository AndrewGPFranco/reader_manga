package com.reader.manga.job.chapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.reader.manga.exception.NotFoundException;
import com.reader.manga.job.base.ColetorBase;
import com.reader.manga.model.Chapter;
import com.reader.manga.model.Manga;
import com.reader.manga.model.Pagina;
import com.reader.manga.repository.ChapterRepository;
import com.reader.manga.repository.PageRepository;
import com.reader.manga.service.MangaService;
import com.reader.manga.vo.job.chapter.ChapterJobVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobChapter extends ColetorBase<ChapterJobVO> {

    private final ChapterRepository capituloRepository;
    private final PageRepository pageRepository;
    private final MangaService mangaService;

    @Override
    protected Mono<ChapterJobVO> executa(Object obj) {
        return null;
    }

    @Override
    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void executa(MultipartFile file, String... varargs) throws IOException {

        if (file == null || file.isEmpty()) {
            log.error("Arquivo MultipartFile é nulo ou vazio!");
            throw new IllegalArgumentException("Arquivo não fornecido!");
        }

        Long mangaId;
        try {
            mangaId = Long.parseLong(varargs[0]);
        } catch (NumberFormatException e) {
            log.error("ID de mangá inválido: {}", varargs[0]);
            throw new IllegalArgumentException("ID de mangá inválido");
        }

        Manga manga = mangaService.findById(mangaId);
        if (manga == null) {
            log.error("Mangá com ID: {} não encontrado!", mangaId);
            throw new NotFoundException("Mangá não encontrado!");
        }

        try (PDDocument document = PDDocument.load(file.getInputStream())) {
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            Chapter capitulo = new Chapter();
            capitulo.setTitle(varargs[1]);
            capitulo.setManga(manga);
            capitulo.setNumberPages(document.getNumberOfPages());
            capituloRepository.save(capitulo);

            List<Pagina> paginas = new ArrayList<>();
            for (int i = 0; i < document.getNumberOfPages(); i++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(i, 300);

                String outputPath = "uploads/" + varargs[1] + "/pagina_" + i + ".png";
                File outputFile = new File(outputPath);

                outputFile.getParentFile().mkdirs();
                ImageIO.write(image, "PNG", outputFile);

                Pagina pagina = new Pagina();
                pagina.setPathPage(outputFile.getAbsolutePath());
                pagina.setChapter(capitulo);
                pageRepository.save(pagina);
                log.info("Página {} salva com sucesso!", i);
                paginas.add(pagina);
            }

            capitulo.setPages(paginas);
            capituloRepository.save(capitulo);
            log.info("Cápitulo {} salvo com sucesso!", varargs[1]);
        }
    }

    @Override
    protected void salvaDadosNoBanco(ChapterJobVO dados) {
        log.info("Implementar");
    }

    @Override
    protected ChapterJobVO deseralizarObjeto(JsonNode atributos) {
        return null;
    }
}
