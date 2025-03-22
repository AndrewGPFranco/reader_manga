package com.reader.manga.domain.valueobjects.users;

import com.reader.manga.domain.enums.StatusType;
import lombok.Builder;
import lombok.Setter;

import java.util.Date;

/**
 * VO auxiliar para transmitir os mangas do usuário, a fim de evitar mandar a entidade.
 * @author andrewgpf
 */
@Builder
public record MangaUserVO(
        Long id,
        String title,
        String image,
        String description,
        Date creationDate,
        Date closingDate,
        @Setter boolean favorite,
        int size,
        StatusType status,
        String author,
        String gender
) {}
