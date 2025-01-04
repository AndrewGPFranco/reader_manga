package com.reader.manga.dto.comment;

import com.reader.manga.enums.CommentCategoryType;
import com.reader.manga.enums.FeedbackType;

public record CommentDTO(
        String nameUser,
        String commentText,
        FeedbackType feedback,
        CommentCategoryType commentCategoryType,
        Long mangaId
) {}
