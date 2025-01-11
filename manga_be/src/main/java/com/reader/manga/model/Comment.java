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
    @Column(name = "name_user")
    // TODO: Improvisado até implementar gerenciamento de login.
    private String nameUser;

    @NotNull
    @NotBlank
    @Column(name = "comment_text")
    private String commentText;

    @NotNull
    @Column(name = "timestamp")
    private LocalDateTime timestamp;

    @NotNull
    @Column(name = "feedback")
    @Enumerated(EnumType.STRING)
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
