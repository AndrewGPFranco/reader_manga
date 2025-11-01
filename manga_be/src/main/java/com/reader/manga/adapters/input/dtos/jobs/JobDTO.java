package com.reader.manga.adapters.input.dtos.jobs;

public record JobDTO(
        String nomeJob,
        String dataIn,
        boolean isAtivo,
        String tipoDoJob,
        boolean isPossuiVersaoAntiga
) {}