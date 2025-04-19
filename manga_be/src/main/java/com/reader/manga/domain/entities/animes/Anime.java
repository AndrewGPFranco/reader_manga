package com.reader.manga.domain.entities.animes;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Builder
@Table(name = "anime")
public class Anime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long size;

    @NotNull
    @NotBlank
    private String title;

    @NotNull
    @Column(name = "upload_date")
    private LocalDate uploadDate;

}