package com.reader.manga.adapters.input.mappers;

import com.reader.manga.adapters.input.dtos.comment.CommentDTO;
import com.reader.manga.adapters.input.dtos.comment.CommentResponseDTO;
import com.reader.manga.domain.entities.animes.VideosComments;
import com.reader.manga.domain.entities.mangas.Comment;
import com.reader.manga.domain.entities.mangas.Manga;
import com.reader.manga.domain.valueobjects.screens.episodes.EpisodeCommentsVO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CommentMapper {

    private CommentMapper() {}

    public static Comment dtoToEntity(CommentDTO commentDTO, Manga manga, String username) {
        return new Comment(username, commentDTO.commentText(),
                LocalDateTime.now(), commentDTO.feedback(), manga);
    }

    public static CommentDTO entityToDto(Comment comment) {
        return new CommentDTO(comment.getCommentText(),
                comment.getFeedback(), comment.getId());
    }

    public static CommentResponseDTO entityToResponseDTO(Comment comment) {
        return new CommentResponseDTO(comment.getNameUser(), comment.getCommentText(),
                comment.getFeedback(), comment.getId());
    }

    public EpisodeCommentsVO entityEpisodeCommentToVO(VideosComments comment) {
        return EpisodeCommentsVO.builder()
                .nameUser(comment.getUser().getUsername())
                .comment(comment.getComment())
                .build();
    }

}
