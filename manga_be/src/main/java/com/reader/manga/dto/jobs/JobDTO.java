package com.reader.manga.dto.jobs;

import lombok.Builder;

@Builder
public record JobDTO(
        String nomeJob,
        String dataIn,
        boolean isAtivo,
        String tipoDoJob,
        boolean isPossuiVersaoAntiga
) {}