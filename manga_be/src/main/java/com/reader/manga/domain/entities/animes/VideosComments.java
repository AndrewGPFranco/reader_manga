package com.reader.manga.domain.entities.animes;

import com.reader.manga.domain.entities.users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

@Data
@Entity
@Builder
@ToString
@AllArgsConstructor
@Table(name = "videos_comments")
public class VideosComments {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private User user;

    @JoinColumn(name = "episode_id", nullable = false)
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    private Episode episode;

    @NotBlank
    @Length(min = 1, max = 2000)
    private String comment;

    public VideosComments() {}
}
