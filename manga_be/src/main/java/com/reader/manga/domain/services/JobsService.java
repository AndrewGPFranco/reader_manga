package com.reader.manga.domain.services;

import com.reader.manga.domain.enums.JobsType;
import com.reader.manga.domain.job.chapter.JobChapter;
import com.reader.manga.domain.job.manga.ColetorMangaFonteI;
import com.reader.manga.domain.valueobjects.jobs.manga.MangaJobVOI;
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

    private final ColetorMangaFonteI coletorManga;
    private final JobChapter jobChapter;

    /**
     * Job que busca informações de um mangá e as salvas.
     * @param manga consultado
     * @return dados salvos do mangá consultado.
     */
    public MangaJobVOI executaJobManga(String manga) {
        Mono<MangaJobVOI> executa = coletorManga.executa(manga);
        return executa.block();
    }

    /**
     * Responsável por levar ao front todos os Jobs disponíveis no sistema.
     * @return lista com todos os Jobs.
     */
    public List<JobsType> getJobs() {
        return Arrays.asList(JobsType.values());
    }

    public void executaJobChapter(MultipartFile path, String titleManga, String titleChapter) throws IOException {
        jobChapter.executa(path, titleManga, titleChapter);
    }

}
