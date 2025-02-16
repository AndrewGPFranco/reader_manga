package com.reader.manga.model;

import com.reader.manga.enums.StatusType;
import com.reader.manga.interfaces.iListaMangasPorUsuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_manga")
@Entity(name = "user_manga")
public class UserManga implements iListaMangasPorUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "manga_id", nullable = false)
    private Manga manga;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "data_assinatura")
    private LocalDate signatureDate;

    @Column(name = "status")
    private StatusType status;

    @Column(name = "nota")
    private Integer nota;

    @Column(name = "comentario")
    private String comment;

}
