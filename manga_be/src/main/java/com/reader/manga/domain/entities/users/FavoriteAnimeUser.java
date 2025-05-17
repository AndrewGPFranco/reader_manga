package com.reader.manga.domain.entities.users;

import com.reader.manga.domain.entities.animes.Anime;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "anime_favorite_user")
@Entity(name = "anime_favorite_user")
public class FavoriteAnimeUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "anime_id", nullable = false)
    private Anime anime;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Override
    public String toString() {
        return "FavoriteAnimeUser{" +
                "id=" + id +
                ", anime=" + anime +
                ", user=" + user +
                '}';
    }

}
