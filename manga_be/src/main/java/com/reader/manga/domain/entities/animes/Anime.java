package com.reader.manga.domain.entities.animes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Builder
@AllArgsConstructor
@Table(name = "anime")
public class Anime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @Column(name = "upload_date")
    private LocalDate uploadDate;

    @JsonIgnore
    @OneToMany(mappedBy = "anime", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Episode> episodes;

    @NotNull
    @NotBlank
    @Column(name = "uri_image")
    private String uriImage;

    @NotNull
    @Column(name = "release_date")
    private LocalDate releaseDate;

    public Anime() {}

    @Override
    public String toString() {
        return "Anime{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", uploadDate=" + uploadDate +
                ", episodes=" + episodes +
                ", uriImage='" + uriImage + '\'' +
                ", releaseDate=" + releaseDate +
                '}';
    }

}