package com.reader.manga.adapters.input.dtos.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Classe auxiliar para recebimento de dados para realização de login
 * @param usernameOrEmail
 * @param password
 */
public record UserLoginDTO(
        @NotNull @NotBlank String usernameOrEmail,
        @NotNull @NotBlank String password,
        String token
) {}
