package com.reader.manga.controller;

import com.reader.manga.enums.JobsType;
import com.reader.manga.service.JobsService;
import com.reader.manga.vo.job.manga.MangaJobVO;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/jobs")
public class JobsController {

    private final JobsService jobsService;

    @GetMapping("/manga-job/{manga}")
    public ResponseEntity<MangaJobVO> executaJob(@PathVariable String manga) {
        return ResponseEntity.ok().body(jobsService.executaJobManga(manga));
    }

    @GetMapping("/get-jobs")
    public ResponseEntity<List<JobsType>> getJobs() {
        List<JobsType> jobs = jobsService.getJobs();
        return ResponseEntity.ok().body(jobs);
    }
}
