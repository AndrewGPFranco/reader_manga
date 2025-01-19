package com.reader.manga.service;

import com.reader.manga.dto.comment.CommentDTO;
import com.reader.manga.dto.comment.CommentResponseDTO;
import com.reader.manga.exception.NotFoundException;
import com.reader.manga.mapper.CommentMapper;
import com.reader.manga.model.Comment;
import com.reader.manga.model.Manga;
import com.reader.manga.repository.CommentRepository;
import com.reader.manga.repository.MangaRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public CommentResponseDTO registerComment(CommentDTO commentDTO, String username) {
        Manga mangaById = mangaService.findById(commentDTO.mangaId());
        Comment comment = CommentMapper.dtoToEntity(commentDTO, mangaById, username);
        Comment commentSaved = commentRepository.save(comment);
        return CommentMapper.entityToResponseDTO(commentSaved);
    }

    /**
     * Responsável por trazer todos os comentários de um mangá específico.
     * @param idManga do mangá a ser resgatados os comentários.
     * @return lista de comentários do mangá pesquisado.
     */
    public List<CommentResponseDTO> getComments(Long idManga) {
        Manga mangaById = mangaService.findById(idManga);
        return mangaById.getComments()
                .stream()
                .map(CommentMapper::entityToResponseDTO)
                .toList();
    }

    /**
     * Responsável por buscar todos os comentários do sistema.
     * @return todos os comentários do sistema.
     */
    public List<CommentResponseDTO> getAllComments() {
        List<CommentResponseDTO> listCommentDto = new ArrayList<>();
        List<Comment> allComments = commentRepository.findAll();

        if(!allComments.isEmpty())
            allComments.forEach(comment -> listCommentDto.add(CommentMapper.entityToResponseDTO(comment)));

        return listCommentDto;
    }

    /**
     * Responsável por remover todos os comentários do sistema.
     */
    public void deleteAllComments() {
        commentRepository.deleteAll();
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
        commentRepository.findById(id)
                .ifPresentOrElse(
                        comment -> commentRepository.deleteById(comment.getId()),
                        () -> { throw new NotFoundException("Nenhum comentário encontrado com o id: " + id); }
                );
    }

    public CommentResponseDTO updateComment(Long id, String commentUser) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nenhum comentário encontrado com o id: " + id));

        UtilsService.updateField(commentUser, comment::setCommentText);

        commentRepository.save(comment);

        return CommentMapper.entityToResponseDTO(comment);
    }
}
