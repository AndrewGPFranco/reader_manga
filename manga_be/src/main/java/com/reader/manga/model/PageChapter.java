package com.reader.manga.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "page")
@NoArgsConstructor
@AllArgsConstructor
public class PageChapter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "page")
    private String chapterPage;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id", nullable = false)
    private Chapter chapter;

    @Column(name = "id_chapter")
    private Long idChapter;

    public PageChapter(String chapterPage, Chapter chapter, Long idChapter) {
        this.chapterPage = chapterPage;
        this.chapter = chapter;
        this.idChapter = idChapter;
    }
}
