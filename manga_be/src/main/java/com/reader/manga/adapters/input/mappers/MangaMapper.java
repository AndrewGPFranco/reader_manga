package com.reader.manga.adapters.input.mappers;

import com.reader.manga.adapters.input.dtos.manga.MangaDTO;
import com.reader.manga.adapters.input.dtos.manga.UpdateMangaDTO;
import com.reader.manga.domain.entities.mangas.Manga;
import com.reader.manga.domain.valueobjects.users.MangaUserVO;
import org.springframework.stereotype.Component;

@Component
public class MangaMapper {

    private MangaMapper() {}

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
                .status(manga.getStatus())
                .build();
    }

    public MangaDTO entityToDto(Manga manga) {
        return MangaDTO.builder()
                .title(manga.getTitle())
                .author(manga.getAuthor())
                .image(manga.getImage())
                .closingDate(manga.getClosingDate())
                .creationDate(manga.getCreationDate())
                .description(manga.getDescription())
                .gender(manga.getGender())
                .status(manga.getStatus())
                .size(manga.getSize())
                .build();
    }

    public MangaUserVO mangaToMangaUserVO(Manga manga, boolean isFavorite) {
        return MangaUserVO.builder()
                .id(manga.getId())
                .title(manga.getTitle())
                .image(manga.getImage())
                .status(manga.getStatus())
                .description(manga.getDescription())
                .creationDate(manga.getCreationDate())
                .closingDate(manga.getClosingDate())
                .author(manga.getAuthor())
                .gender(manga.getGender())
                .favorite(isFavorite)
                .size(manga.getSize())
                .build();
    }
}
