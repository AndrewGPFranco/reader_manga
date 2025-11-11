package com.reader.manga.domain.enums;

import lombok.Getter;

@Getter
public enum OriginType {
    MANGA("Mangá"),
    ANIME("Anime");

    private final String description;

    OriginType(String description) {
        this.description = description;
    }

}
