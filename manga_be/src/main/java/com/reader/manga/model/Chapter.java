package com.reader.manga.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manga_id", nullable = false)
    private Manga manga;

    @JsonIgnore
    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Page> pages;

    public Chapter(String title, String description, Integer numberPages, Manga manga) {
        this.title = title;
        this.description = description;
        this.numberPages = numberPages;
        this.manga = manga;
    }

}
