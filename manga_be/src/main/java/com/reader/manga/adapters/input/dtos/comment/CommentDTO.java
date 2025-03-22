package com.reader.manga.adapters.input.dtos.comment;

import com.reader.manga.domain.enums.FeedbackType;

public record CommentDTO(
        String commentText,
        FeedbackType feedback,
        Long mangaId
) {}
