package com.reader.manga.domain.entities.users;

import com.reader.manga.domain.entities.mangas.Manga;
import com.reader.manga.domain.enums.StatusType;
import com.reader.manga.domain.interfaces.iListaMangasPorUsuario;
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

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "manga_id", nullable = false)
    private Manga manga;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "data_assinatura")
    private LocalDate signatureDate;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusType status;

    @Column(name = "nota")
    private Integer nota;

    @Column(name = "comentario")
    private String comment;

    @Override
    public String toString() {
        return "UserManga{" +
                "id=" + id +
                ", manga=" + manga +
                ", user=" + user +
                ", signatureDate=" + signatureDate +
                ", status=" + status +
                ", nota=" + nota +
                ", comment='" + comment + '\'' +
                '}';
    }

}
