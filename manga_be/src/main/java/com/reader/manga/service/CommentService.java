package com.reader.manga.service;

import com.reader.manga.dto.comment.CommentDTO;
import com.reader.manga.exception.NotFoundException;
import com.reader.manga.mapper.CommentMapper;
import com.reader.manga.model.Comment;
import com.reader.manga.model.Manga;
import com.reader.manga.repository.CommentRepository;
import com.reader.manga.repository.MangaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final MangaRepository mangaRepository;
    private final MangaService mangaService;

    public CommentService(CommentRepository commentRepository, MangaRepository mangaRepository, MangaService mangaService) {
        this.commentRepository = commentRepository;
        this.mangaRepository = mangaRepository;
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

    /**
     * Responsável por trazer todos os comentários de um mangá específico.
     * @param idManga do mangá a ser resgatados os comentários.
     * @return lista de comentários do mangá pesquisado.
     */
    public List<CommentDTO> getComments(Long idManga) {
        try {
            Manga mangaById = mangaService.findById(idManga);
            List<CommentDTO> listCommentDto = new ArrayList<>();
            mangaById.getComments().forEach(comment -> listCommentDto.add(CommentMapper.entityToDto(comment)));
            return listCommentDto;
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    /**
     * Responsável por buscar todos os comentários do sistema.
     * @return todos os comentários do sistema.
     */
    public List<CommentDTO> getAllComments() {
        List<CommentDTO> listCommentDto = new ArrayList<>();
        List<Comment> allComments = commentRepository.findAll();

        if(!allComments.isEmpty())
            allComments.forEach(comment -> listCommentDto.add(CommentMapper.entityToDto(comment)));

        return listCommentDto;
    }

    /**
     * Responsável por remover todos os comentários do sistema.
     */
    public void deleteAllComments() {
        List<Comment> allComments = commentRepository.findAll();
        if(!allComments.isEmpty())
            allComments.forEach(comment -> commentRepository.deleteById(comment.getId()));
    }

    /**
     * Responsável por excluir todos os comentários de um mangá
     * @param idManga do mangá a ter os comentários excluídos.
     */
    public void deleteAllCommentsById(Long idManga) {
        try {
            Manga mangaById = mangaService.findById(idManga);
            mangaById.getComments().removeAll(mangaById.getComments());
            mangaRepository.save(mangaById);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    /**
     * Deletar um comentário pelo id.
     * @param id do comentário a ser excluído.
     */
    public void deleteCommentById(Long id) {
        Optional<Comment> commentById = commentRepository.findById(id);
        if(commentById.isPresent())
            commentRepository.deleteById(id);
        else
            throw new NotFoundException("Nenhum comentário encontrado com o id: " + id);
    }

    public CommentDTO updateComment(Long id, String commentUser) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nenhum comentário encontrado com o id: " + id));

        UtilsService.updateField(commentUser, comment::setCommentText);

        commentRepository.save(comment);

        return CommentMapper.entityToDto(comment);
    }
}
