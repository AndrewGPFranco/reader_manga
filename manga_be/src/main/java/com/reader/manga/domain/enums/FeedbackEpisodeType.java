package com.reader.manga.domain.enums;

import lombok.Getter;

@Getter
public enum FeedbackEpisodeType {

    NOTHING("Sem feedback"),
    LIKE("Gostei"),
    DISLIKE("Não gostei");

    private final String description;

    FeedbackEpisodeType(String description) {
        this.description = description;
    }

}
