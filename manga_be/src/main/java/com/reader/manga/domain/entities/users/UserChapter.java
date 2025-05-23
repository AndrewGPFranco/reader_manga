package com.reader.manga.domain.entities.users;

import com.reader.manga.domain.entities.mangas.Chapter;
import com.reader.manga.domain.enums.StatusType;
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
@Table(name = "user_chapter")
public class UserChapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user_id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "chapter_id", nullable = false)
    private Chapter chapter_id;

    @Column(name = "progress", nullable = false)
    private Integer progress;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusType status = StatusType.ONGOING;

    public UserChapter() {}
}
