package com.reader.manga.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RecoveryJwtTokenDto(
        @NotNull @NotBlank String token
) {}
