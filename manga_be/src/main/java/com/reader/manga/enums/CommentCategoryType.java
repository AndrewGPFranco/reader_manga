package com.reader.manga.enums;

import lombok.Getter;

@Getter
public enum CommentCategoryType {
    MANGA("Mangá"),
    CHAPTER("Capítulo");

    private final String specialty;

    CommentCategoryType(String category) {
        this.specialty = category;
    }

}
