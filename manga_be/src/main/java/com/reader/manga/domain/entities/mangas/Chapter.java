package com.reader.manga.domain.entities.mangas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.reader.manga.domain.enums.StatusType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name = "chapter")
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "title", unique = true)
    @Length(min = 3, max = 30)
    private String title;

    @NotNull
    @Column(name = "number_pages")
    private Integer numberPages = 0;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "manga_id", nullable = false)
    private Manga manga;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Pagina> pages;

    @NotNull
    @Enumerated(EnumType.STRING)
    private StatusType status = StatusType.ONGOING;

    @NotNull
    private Integer readingProgress = 0;

    public Chapter(String title, Integer numberPages, Manga manga) {
        this.title = title;
        this.numberPages = numberPages;
        this.manga = manga;
    }

    @Override
    public String toString() {
        return "Chapter{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", numberPages=" + numberPages +
                ", manga=" + manga +
                ", status=" + status +
                ", readingProgress=" + readingProgress +
                '}';
    }

}
