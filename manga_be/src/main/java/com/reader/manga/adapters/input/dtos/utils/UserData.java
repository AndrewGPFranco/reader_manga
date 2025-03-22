package com.reader.manga.adapters.input.dtos.utils;

/**
 * DTO responsável por obter dados necessários para diversas ações no sistema envolvendo o usuário da sessão.
 * Não sendo obrigatório o envio de todos os dados, somente o que a operação necessitar.
 */
public record UserData(
        Long idUser,
        Long idManga,
        Long idChapter,
        Long idPage
) {}
