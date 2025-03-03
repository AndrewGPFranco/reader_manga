package com.reader.manga.enums;

import lombok.Getter;

/**
 * Enum com os Jobs disponíveis no sistema.
 */
@Getter
public enum JobsType {
    MANGA("Manga", "15/02/2025", true, false, "Parâmetros"),
    CHAPTER("Chapter", "02/03/2025", true, false, "Uploads");

    private final String nomeJob;
    private final String dataIn;
    private final boolean isAtivo;
    private final String tipoDoJob;
    private final boolean isPossuiVersaoAntiga;

    JobsType(String nomeJob, String dataIn, boolean isAtivo, boolean isPossuiVersaoAntiga, String tipoDoJob) {
        this.nomeJob = nomeJob;
        this.dataIn = dataIn;
        this.isAtivo = isAtivo;
        this.isPossuiVersaoAntiga = isPossuiVersaoAntiga;
        this.tipoDoJob = tipoDoJob;
    }

}
