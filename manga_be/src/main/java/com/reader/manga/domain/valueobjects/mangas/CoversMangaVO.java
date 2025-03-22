package com.reader.manga.domain.valueobjects.mangas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CoversMangaVO {

    private String titulo;

    private String urlImage;

    private String urlReader;

}
