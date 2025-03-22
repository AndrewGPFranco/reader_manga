package com.reader.manga.domain.valueobjects.jobs.manga;

import com.reader.manga.domain.enums.StatusType;
import com.reader.manga.domain.interfaces.iDadosManga;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class MangaJobVOI implements iDadosManga {

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
