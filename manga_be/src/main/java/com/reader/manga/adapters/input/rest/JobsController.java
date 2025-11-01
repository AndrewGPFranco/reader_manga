package com.reader.manga.adapters.input.rest;

import com.reader.manga.adapters.input.dtos.jobs.JobDTO;
import com.reader.manga.domain.enums.JobsType;
import com.reader.manga.domain.interfaces.iDadosManga;
import com.reader.manga.domain.job.chapter.JobChapter;
import com.reader.manga.domain.services.JobsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/job")
public class JobsController <T> implements iDadosManga {

    private final JobChapter jobChapter;
    private final JobsService jobsService;

    @PostMapping("/{name}/{parametros}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<T> executaJob(@PathVariable JobsType name, @PathVariable String parametros) {
        T responseJobVO;
        if (Objects.requireNonNull(name) == JobsType.MANGA) {
            responseJobVO = (T) jobsService.executaJobManga(parametros);
        } else {
            throw new IllegalArgumentException("Tipo de job inválido: " + name);
        }
        return ResponseEntity.ok().body(responseJobVO);
    }

    @GetMapping("/get-jobs")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<JobDTO>> getJobs() {
        List<JobsType> jobs = jobsService.getJobs();
        List<JobDTO> jobDtoList = new ArrayList<>(jobs.size());
        jobs.forEach(job ->
                jobDtoList.add(new JobDTO(job.getNomeJob(), job.getDataIn(),
                        job.isAtivo(), job.getTipoDoJob(), job.isPossuiVersaoAntiga())));

        return ResponseEntity.ok().body(jobDtoList);
    }

    @PostMapping("/upload-chapter")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> executaJobChapter(
            @RequestParam("file") MultipartFile file,
            @RequestParam("titleChapter") String titleChapter,
            @RequestParam("titleManga") String titleManga) {
        try {
            jobsService.executaJobChapter(file, titleManga, titleChapter);

            return ResponseEntity.ok("Job finalizado com sucesso");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar o capítulo: " + e.getMessage());
        }
    }

    @GetMapping(value = "/sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe() {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        jobChapter.addEmitter(emitter);
        return emitter;
    }

}
