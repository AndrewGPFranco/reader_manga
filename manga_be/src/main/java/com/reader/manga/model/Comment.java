package com.reader.manga.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reader.manga.enums.FeedbackType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comment")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    // TODO: Improvisado até implementar gerenciamento de login.
    private String nameUser;

    @NotNull
    @NotBlank
    private String commentText;

    @NotNull
    private LocalDateTime timestamp;

    @NotNull
    private FeedbackType feedback;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manga_id", nullable = false)
    private Manga manga;

    public Comment(String nameUser, String commentText, LocalDateTime timestamp, FeedbackType feedback, Manga manga) {
        this.nameUser = nameUser;
        this.commentText = commentText;
        this.timestamp = timestamp;
        this.feedback = feedback;
        this.manga = manga;
    }
}
