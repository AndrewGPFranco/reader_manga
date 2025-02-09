package com.reader.manga.vo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * VO auxiliar para a alteração de senha do usuário.
 * @param oldPassword
 * @param newPassword
 * @param idUser
 */
public record ChangePasswordVO(
        @NotNull @NotBlank String oldPassword,
        @NotNull @NotBlank String newPassword,
        @NotNull Long idUser
) {}
