package com.reader.manga.service.jobs;

import com.reader.manga.enums.JobsType;
import com.reader.manga.job.chapter.JobChapter;
import com.reader.manga.job.manga.ColetorMangaFonte;
import com.reader.manga.vo.job.manga.MangaJobVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class JobsService {

    private final ColetorMangaFonte coletorManga;
    private final JobChapter jobChapter;

    /**
     * Job que busca informações de um mangá e as salvas.
     * @param manga consultado
     * @return dados salvos do mangá consultado.
     */
    public MangaJobVO executaJobManga(String manga) {
        Mono<MangaJobVO> executa = coletorManga.executa(manga);
        return executa.block();
    }

    /**
     * Responsável por levar ao front todos os Jobs dísponiveis no sistema.
     * @return lista com todos os Jobs.
     */
    public List<JobsType> getJobs() {
        return Arrays.asList(JobsType.values());
    }

    public void executaJobChapter(MultipartFile path, String manga, String titleChapter) throws IOException {
        jobChapter.executa(path, manga, titleChapter);
    }

}
