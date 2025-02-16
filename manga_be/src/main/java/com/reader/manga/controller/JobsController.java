package com.reader.manga.controller;

import com.reader.manga.enums.JobsType;
import com.reader.manga.interfaces.DadosManga;
import com.reader.manga.service.JobsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
