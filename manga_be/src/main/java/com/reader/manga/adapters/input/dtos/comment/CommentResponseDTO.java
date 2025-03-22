package com.reader.manga.adapters.input.dtos.comment;

import com.reader.manga.domain.enums.FeedbackType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentResponseDTO(
        @NotNull @NotBlank String username,
        @NotNull @NotBlank String commentText,
        @NotNull FeedbackType feedback,
        @NotNull Long mangaId
) {}
