package com.reader.manga.controller;

import com.reader.manga.dto.comment.CommentDTO;
import com.reader.manga.dto.comment.CommentResponseDTO;
import com.reader.manga.service.CommentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/new/manga")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<CommentResponseDTO> registerCommentForManga(@RequestBody CommentDTO dto, Principal principal) {
        CommentResponseDTO comment = commentService.registerComment(dto, principal.getName());
        return ResponseEntity.ok().body(comment);
    }

    @GetMapping("/manga/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByManga(@PathVariable Long id) {
        List<CommentResponseDTO> comments = commentService.getComments(id);
        return ResponseEntity.ok().body(comments);
    }

    @GetMapping("/manga")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<CommentResponseDTO>> getCommentsByManga() {
        List<CommentResponseDTO> comments = commentService.getAllComments();
        return ResponseEntity.ok().body(comments);
    }

    @DeleteMapping("/all-manga")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteAllComments() {
        commentService.deleteAllComments();
    }

    @DeleteMapping("/delete/all-manga/{idManga}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteAllCommentsById(@PathVariable Long idManga) {
        commentService.deleteAllCommentsById(idManga);
    }

    @DeleteMapping("/delete/{idManga}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteCommentById(@PathVariable Long idManga) {
        commentService.deleteCommentById(idManga);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<CommentResponseDTO> updateComment(@PathVariable Long id, @RequestBody CommentDTO comment) {
        CommentResponseDTO commentDto = commentService.updateComment(id, comment.commentText());
        return ResponseEntity.ok().body(commentDto);
    }
}
