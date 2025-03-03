package com.reader.manga.vo.job.chapter;

import com.reader.manga.interfaces.DadosManga;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChapterJobVO implements DadosManga {

    // Atributos para o capítulo
    private String titleChapter;
    private String descriptionChapter;
    private Integer numberPages;
    private String manga;

    // Atributos para a página
    private String urlChapterPage;
    private Long idChapter;

}
