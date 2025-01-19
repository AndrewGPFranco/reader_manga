package com.reader.manga.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Classe auxiliar para recebimento de dados para realização de login
 * @param email
 * @param password
 */
public record UserLoginDTO(
        @NotNull @NotBlank String email,
        @NotNull @NotBlank String password,
        String token
) {}
