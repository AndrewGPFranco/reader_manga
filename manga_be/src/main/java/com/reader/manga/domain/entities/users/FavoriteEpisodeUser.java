package com.reader.manga.domain.entities.users;

import com.reader.manga.domain.entities.animes.Episode;
import com.reader.manga.domain.enums.FeedbackEpisodeType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Builder
@ToString
@AllArgsConstructor
@Table(name = "favorite_episode")
public class FavoriteEpisodeUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    private FeedbackEpisodeType feedback;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "episode_id", nullable = false)
    private Episode episode;

    public FavoriteEpisodeUser() {}
}
