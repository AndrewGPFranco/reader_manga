package com.reader.manga.domain.valueobjects.mangas;

import lombok.Builder;

@Builder
public record CoversMangaVO(
        String titulo,
        String urlImage,
        String urlReader
) {}
