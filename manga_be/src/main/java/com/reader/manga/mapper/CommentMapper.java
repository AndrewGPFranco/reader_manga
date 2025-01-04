package com.reader.manga.mapper;

import com.reader.manga.dto.comment.CommentDTO;
import com.reader.manga.model.Comment;
import com.reader.manga.model.Manga;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CommentMapper {

    private CommentMapper() {}

    public static Comment dtoToEntity(CommentDTO commentDTO, Manga manga) {
        return new Comment(commentDTO.nameUser(), commentDTO.commentText(),
                LocalDateTime.now(), commentDTO.feedback(), manga);
    }

    public static CommentDTO entityToDto(Comment comment) {
        return new CommentDTO(comment.getNameUser(), comment.getCommentText(),
                comment.getFeedback(), comment.getId());
    }

}
