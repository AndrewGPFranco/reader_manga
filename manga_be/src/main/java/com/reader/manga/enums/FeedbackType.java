package com.reader.manga.enums;

import lombok.Getter;

@Getter
public enum FeedbackType {
    REALLY_LIKED("Gostei muito"),
    LIKED("Gostei"),
    MORE_OR_LESS("Mais ou menos"),
    HATED("Não gostei");

    private final String feedback;

    FeedbackType(String feedback) {
        this.feedback = feedback;
    }
}
