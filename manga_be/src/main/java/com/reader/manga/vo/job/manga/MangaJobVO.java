package com.reader.manga.vo.job.manga;

import com.reader.manga.enums.StatusType;
import com.reader.manga.interfaces.DadosManga;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class MangaJobVO implements DadosManga {

    private String title;
    private String description;
    private Integer size;
    private Date creationDate;
    private Date closingDate;
    private StatusType status;
    private String author;
    private String gender;
    private String image;

}
