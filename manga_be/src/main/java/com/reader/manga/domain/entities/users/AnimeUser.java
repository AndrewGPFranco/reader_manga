package com.reader.manga.domain.entities.users;

import com.reader.manga.domain.entities.animes.Anime;
import com.reader.manga.domain.enums.StatusType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "anime_user")
public class AnimeUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    private User userId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "anime_id", nullable = false, referencedColumnName = "id")
    private Anime animeId;

    private Integer note;

    private Integer progress;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusType status;

    public AnimeUser(User userId, Anime animeId, Integer note, Integer progress, @NotNull StatusType status) {
        this.userId = userId;
        this.animeId = animeId;
        this.note = note;
        this.progress = progress;
        this.status = status;
    }

    @Override
    public String toString() {
        return "AnimeUser{" +
                "id=" + id +
                ", userId=" + userId +
                ", animeId=" + animeId +
                ", note=" + note +
                ", progress=" + progress +
                ", status=" + status +
                '}';
    }

}