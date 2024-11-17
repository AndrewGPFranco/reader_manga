package com.reader.manga.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class MangaCoverVO {

    private String id;
    private String imageUrl;
}
