package com.reader.manga.domain.valueobjects.jobs.chapter;

import com.reader.manga.domain.interfaces.iDadosManga;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChapterJobVO implements iDadosManga {

    // Atributos para o capítulo
    private String titleChapter;
    private String descriptionChapter;
    private Integer numberPages;
    private String manga;

    // Atributos para a página
    private String urlChapterPage;
    private Long idChapter;

}
