package com.reader.manga.controller;

import com.reader.manga.dto.comment.CommentDTO;
import com.reader.manga.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/new/manga")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CommentDTO> registerCommentForManga(@RequestBody CommentDTO dto) {
        CommentDTO comment = commentService.registerComment(dto);
        return ResponseEntity.ok().body(comment);
    }
}
