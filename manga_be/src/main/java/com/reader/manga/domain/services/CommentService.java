package com.reader.manga.domain.services;

import com.reader.manga.adapters.input.dtos.comment.CommentDTO;
import com.reader.manga.adapters.input.dtos.comment.CommentResponseDTO;
import com.reader.manga.domain.exceptions.NotFoundException;
import com.reader.manga.adapters.input.mappers.CommentMapper;
import com.reader.manga.domain.entities.mangas.Comment;
import com.reader.manga.domain.entities.mangas.Manga;
import com.reader.manga.ports.repositories.CommentRepository;
import com.reader.manga.ports.repositories.MangaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository jpaCommentRepository;
    private final MangaRepository jpaMangaRepository;
    private final MangaService mangaService;

    public CommentResponseDTO registerComment(CommentDTO commentDTO, String username) {
        Manga mangaById = mangaService.findById(commentDTO.mangaId());
        Comment comment = CommentMapper.dtoToEntity(commentDTO, mangaById, username);
        Comment commentSaved = jpaCommentRepository.save(comment);
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
        List<Comment> allComments = jpaCommentRepository.findAll();

        if(!allComments.isEmpty())
            allComments.forEach(comment -> listCommentDto.add(CommentMapper.entityToResponseDTO(comment)));

        return listCommentDto;
    }

    /**
     * Responsável por remover todos os comentários do sistema.
     */
    public void deleteAllComments() {
        jpaCommentRepository.deleteAll();
    }

    /**
     * Responsável por excluir todos os comentários de um mangá
     * @param idManga do mangá a ter os comentários excluídos.
     */
    public void deleteAllCommentsById(Long idManga) {
        try {
            Manga mangaById = mangaService.findById(idManga);
            mangaById.getComments().removeAll(mangaById.getComments());
            jpaMangaRepository.save(mangaById);
        } catch (NotFoundException e) {
            throw new NotFoundException(e.getMessage());
        }
    }

    /**
     * Deletar um comentário pelo id.
     * @param id do comentário a ser excluído.
     */
    public void deleteCommentById(Long id) {
        jpaCommentRepository.findById(id)
                .ifPresentOrElse(
                        comment -> jpaCommentRepository.deleteById(comment.getId()),
                        () -> { throw new NotFoundException("Nenhum comentário encontrado com o id: " + id); }
                );
    }

    public CommentResponseDTO updateComment(Long id, String commentUser) {
        Comment comment = jpaCommentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nenhum comentário encontrado com o id: " + id));

        UtilsService.updateField(commentUser, comment::setCommentText);

        jpaCommentRepository.save(comment);

        return CommentMapper.entityToResponseDTO(comment);
    }
}
