package com.reader.manga.mapper;

import com.reader.manga.dto.manga.UpdateMangaDTO;
import com.reader.manga.model.Manga;
import org.springframework.stereotype.Component;

@Component
public class MangaMapper {

    public UpdateMangaDTO mangaToUpdateMangaDTO(Manga manga) {
        return UpdateMangaDTO.builder()
                .title(manga.getTitle())
                .image(manga.getImage())
                .size(manga.getSize())
                .author(manga.getAuthor())
                .closingDate(manga.getClosingDate())
                .creationDate(manga.getCreationDate())
                .description(manga.getDescription())
                .gender(manga.getGender())
                .isFavorite(manga.isFavorite())
                .status(manga.getStatus())
                .build();
    }
}
