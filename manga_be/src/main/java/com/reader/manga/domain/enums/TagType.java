package com.reader.manga.domain.enums;

import lombok.Getter;

public enum TagType {
    HD("HD"),
    UHD("Ultra HD"),
    NOVO("Novo"),
    ANIME("Anime"),
    MANGA("Manga"),
    ;

    @Getter
    private final String description;

    TagType(String description) {
        this.description = description;
    }

}