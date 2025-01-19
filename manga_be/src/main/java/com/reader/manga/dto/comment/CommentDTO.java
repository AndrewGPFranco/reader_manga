package com.reader.manga.dto.comment;

import com.reader.manga.enums.FeedbackType;

public record CommentDTO(
        String commentText,
        FeedbackType feedback,
        Long mangaId
) {}
