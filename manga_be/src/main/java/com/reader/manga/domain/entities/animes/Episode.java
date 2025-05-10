package com.reader.manga.domain.entities.animes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reader.manga.domain.entities.users.FavoriteEpisodeUser;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Builder
@ToString
@AllArgsConstructor
@Table(name = "episode")
public class Episode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @NotBlank
    private String uri;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anime_id", nullable = false)
    private Anime anime;

    @Min(1)
    @NotNull
    private Integer numberEpisode;

    @NotNull
    private Integer views;

    @NotNull
    private LocalDate uploaded;

    @OneToMany(mappedBy = "episode", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<VideosComments> commentsList;

    public Episode() {
    }

}
