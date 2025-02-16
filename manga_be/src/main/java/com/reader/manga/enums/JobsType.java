package com.reader.manga.enums;

import lombok.Getter;

/**
 * Enum com os Jobs disponíveis no sistema.
 */
@Getter
public enum JobsType {
    MANGA("Manga", "15/02/2025", true, false);

    private final String nomeJob;
    private final String dataIn;
    private final boolean isAtivo;
    private final boolean isPossuiVersaoAntiga;

    JobsType(String nomeJob, String dataIn, boolean isAtivo, boolean isPossuiVersaoAntiga) {
        this.nomeJob = nomeJob;
        this.dataIn = dataIn;
        this.isAtivo = isAtivo;
        this.isPossuiVersaoAntiga = isPossuiVersaoAntiga;
    }

}
