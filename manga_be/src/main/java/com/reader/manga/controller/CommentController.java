package com.reader.manga.controller;

import com.reader.manga.dto.comment.CommentDTO;
import com.reader.manga.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/manga/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CommentDTO>> getCommentsByManga(@PathVariable Long id) {
        List<CommentDTO> comments = commentService.getComments(id);
        return ResponseEntity.ok().body(comments);
    }

    @GetMapping("/manga")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<CommentDTO>> getCommentsByManga() {
        List<CommentDTO> comments = commentService.getAllComments();
        return ResponseEntity.ok().body(comments);
    }

    @DeleteMapping("/all-manga")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAllComments() {
        commentService.deleteAllComments();
    }

    @DeleteMapping("/delete/all-manga/{idManga}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAllCommentsById(@PathVariable Long idManga) {
        commentService.deleteAllCommentsById(idManga);
    }

    @DeleteMapping("/delete/{idManga}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteCommentById(@PathVariable Long idManga) {
        commentService.deleteCommentById(idManga);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Long id, @RequestBody CommentDTO comment) {
        CommentDTO commentDto = commentService.updateComment(id, comment.commentText());
        return ResponseEntity.ok().body(commentDto);
    }
}
