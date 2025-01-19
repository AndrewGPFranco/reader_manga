package com.reader.manga.dto.comment;

import com.reader.manga.enums.FeedbackType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentResponseDTO(
        @NotNull @NotBlank String username,
        @NotNull @NotBlank String commentText,
        @NotNull FeedbackType feedback,
        @NotNull Long mangaId
) {}
