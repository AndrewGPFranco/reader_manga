package com.reader.manga.controller;

import com.reader.manga.enums.JobsType;
import com.reader.manga.interfaces.DadosManga;
import com.reader.manga.service.jobs.JobsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/job")
public class JobsController <T> implements DadosManga {

    private final JobsService jobsService;

    @PostMapping("/{name}/{parametros}")
    public ResponseEntity<T> executaJob(@PathVariable JobsType name, @PathVariable String parametros) {
        T responseJobVO;
        switch(name) {
            case MANGA:
                responseJobVO = (T) jobsService.executaJobManga(parametros);
                break;
            default:
                throw new IllegalArgumentException("Tipo de job inválido: " + name);
        }
        return ResponseEntity.ok().body(responseJobVO);
    }

    @GetMapping("/get-jobs")
    public ResponseEntity<List<JobsType>> getJobs() {
        List<JobsType> jobs = jobsService.getJobs();
        return ResponseEntity.ok().body(jobs);
    }

    @PostMapping("/upload-chapter")
    public ResponseEntity<Object> executaJobChapter(
            @RequestParam("file") MultipartFile file,
            @RequestParam("titleManga") String titleManga,
            @RequestParam("titleChapter") String titleChapter) {
        try {
            jobsService.executaJobChapter(file, titleManga, titleChapter);

            return ResponseEntity.ok(new Object());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Erro ao processar o capítulo: " + e.getMessage());
        }
    }

    @GetMapping("/get-pages")
    public List<String> getPages() {
        return new ArrayList<>();
    }

}
