package com.reader.manga.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "chapter")
public class Chapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "title")
    @Length(min = 3, max = 30)
    private String title;

    @NotNull
    @NotBlank
    @Column(name = "description")
    @Length(min = 10, max = 255)
    private String description;

    @NotNull
    @Column(name = "number_pages")
    @Min(1)
    private Integer numberPages;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manga_id", nullable = false)
    private Manga manga;

    public Chapter(String title, String description, Integer numberPages) {
        this.title = title;
        this.description = description;
        this.numberPages = numberPages;
    }

    public Chapter(String title, String description, Integer numberPages, Manga manga) {
        this.title = title;
        this.description = description;
        this.numberPages = numberPages;
        this.manga = manga;
    }

}
