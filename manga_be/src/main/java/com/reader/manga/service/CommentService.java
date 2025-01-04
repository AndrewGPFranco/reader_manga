package com.reader.manga.service;

import com.reader.manga.dto.comment.CommentDTO;
import com.reader.manga.exception.NotFoundException;
import com.reader.manga.mapper.CommentMapper;
import com.reader.manga.model.Comment;
import com.reader.manga.model.Manga;
import com.reader.manga.repository.CommentRepository;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final MangaService mangaService;

    public CommentService(CommentRepository commentRepository, MangaService mangaService) {
        this.commentRepository = commentRepository;
        this.mangaService = mangaService;
    }

    public CommentDTO registerComment(CommentDTO commentDTO) {
        try {
            Manga mangaById = mangaService.findById(commentDTO.mangaId());
            Comment comment = CommentMapper.dtoToEntity(commentDTO, mangaById);
            Comment commentSaved = commentRepository.save(comment);
            return CommentMapper.entityToDto(commentSaved);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

}
