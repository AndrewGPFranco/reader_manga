package com.reader.manga.job.chapter;

import com.fasterxml.jackson.databind.JsonNode;
import com.reader.manga.job.base.ColetorBase;
import com.reader.manga.repository.ChapterRepository;
import com.reader.manga.service.MangaService;
import com.reader.manga.vo.job.chapter.ChapterJobVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class JobChapter extends ColetorBase<ChapterJobVO> {

    private final ChapterRepository chapterRepository;
    private final MangaService mangaService;

    @Override
    protected Mono<ChapterJobVO> executa(Object obj) {
        return null;
    }

    @Override
    public void executa(String... varargs) throws IOException {

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
