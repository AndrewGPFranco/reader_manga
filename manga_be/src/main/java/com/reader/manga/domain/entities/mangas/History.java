package com.reader.manga.domain.entities.mangas;

import com.reader.manga.domain.enums.StatusType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Entity
@EqualsAndHashCode
@AllArgsConstructor
@Table(name = "history_mangas_user",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "idUser", "idCapitulo", "idManga"
                })
        }
)
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long idUser;

    @NotNull
    @Column(name = "chapter_id", nullable = false)
    private Long idCapitulo;

    @NotNull
    @Column(name = "manga_id", nullable = false)
    private Long idManga;

    @NotNull
    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusType statusType = StatusType.ONGOING;

    @NotNull
    @Column(name = "last_check", nullable = false)
    private OffsetDateTime lastCheck;

    public History() {/* Construtor vazio */}

    public History(Long idUser, Long idCapitulo, Long idManga, StatusType statusType, OffsetDateTime lastCheck) {
        this.idUser = idUser;
        this.idCapitulo = idCapitulo;
        this.idManga = idManga;
        this.statusType = statusType;
        this.lastCheck = lastCheck;
    }

    @Override
    public String toString() {
        return "History{" +
                "id=" + id +
                ", idUser=" + idUser +
                ", idCapitulo=" + idCapitulo +
                ", idManga=" + idManga +
                ", statusType=" + statusType +
                ", lastCheck=" + lastCheck +
                '}';
    }

}
