package com.reader.manga.adapters.input.dtos.jobs;

import lombok.Builder;

@Builder
public record JobDTO(
        String nomeJob,
        String dataIn,
        boolean isAtivo,
        String tipoDoJob,
        boolean isPossuiVersaoAntiga
) {}