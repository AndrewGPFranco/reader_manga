package com.reader.manga.vo;

import com.reader.manga.enums.StatusType;
import lombok.Builder;

/**
 * VO auxiliar para transmitir os mangas do usuário, a fim de evitar mandar a entidade.
 * @author andrewgpf
 */
@Builder
public record MangaUserVO(
        Long id,
        String title,
        String image,
        boolean favorite,
        int size,
        StatusType status,
        String author,
        String gender
) {}
