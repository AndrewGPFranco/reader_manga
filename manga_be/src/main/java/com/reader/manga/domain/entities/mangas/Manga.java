package com.reader.manga.domain.entities.mangas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.reader.manga.domain.enums.StatusType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.Date;
import java.util.List;

@Data
@Entity
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "manga")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Manga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(name = "title")
    @Length(min = 2, max = 100)
    private String title;

    @NotNull
    @NotBlank
    @Column(name = "description")
    @Length(min = 10, max = 5000)
    private String description;

    @NotNull
    @Column(name = "size_manga")
    @Min(value = 1)
    private Integer size;

    @NotNull
    @Column(name = "creation_date")
    private Date creationDate;

    @Column(name = "closing_date")
    private Date closingDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusType status;

    @NotNull
    @NotBlank
    @Column(name = "author")
    @Length(min = 3, max = 25)
    private String author;

    @NotNull
    @NotBlank
    @Column(name = "gender")
    @Length(min = 3, max = 15)
    private String gender;

    @NotNull
    @NotBlank
    @Column(name = "image")
    @Length(min = 10, max = 255)
    private String image;

    @JsonIgnore
    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Chapter> chapters;

    @JsonIgnore
    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Comment> comments;

    @Column(name = "favorite")
    private boolean isFavorite = false;

    public Manga(String title, String description, Integer size, Date creationDate, Date closingDate, StatusType status, String gender, String author, String image) {
        this.title = title;
        this.description = description;
        this.size = size;
        this.creationDate = creationDate;
        this.closingDate = closingDate;
        this.status = status;
        this.gender = gender;
        this.author = author;
        this.image = image;
    }

}
