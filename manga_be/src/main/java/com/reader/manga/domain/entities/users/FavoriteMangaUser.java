package com.reader.manga.domain.entities.users;

import com.reader.manga.domain.entities.mangas.Manga;
import com.reader.manga.domain.interfaces.iListaMangasPorUsuario;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "manga_favorite_user")
@Entity(name = "manga_favorite_user")
public class FavoriteMangaUser implements iListaMangasPorUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "manga_id", nullable = false)
    private Manga manga;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;



}
