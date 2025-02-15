package com.reader.manga.vo.job.manga;

import com.reader.manga.enums.StatusType;
import lombok.Builder;
import lombok.Getter;

import java.util.Date;

@Builder
@Getter
public class MangaJobVO {

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
