package com.reader.manga.domain.valueobjects.users;

import com.reader.manga.domain.enums.StatusType;
import lombok.Builder;

import java.util.Date;

/**
 * VO auxiliar para transmitir os mangas do usuário, a fim de evitar mandar a entidade.
 * @author andrewgpf
 */
@Builder
public record MangaUserVO(
        Long id,
        int size,
        String title,
        String image,
        Integer nota,
        String author,
        String gender,
        Date closingDate,
        StatusType status,
        Date creationDate,
        String description,
        boolean favorite
) {}
