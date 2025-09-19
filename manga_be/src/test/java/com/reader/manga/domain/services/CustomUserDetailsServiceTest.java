package com.reader.manga.domain.services;

import com.reader.manga.adapters.output.repositories.JpaUserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

class CustomUserDetailsServiceTest {

    @Spy
    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Mock
    private JpaUserRepository jpaUserRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Verifica se o email está digitado conforme o padrão")
    void testIsEmail() {
        boolean result1 = customUserDetailsService.isEmail("andrew@gmail.com");
        Assertions.assertTrue(result1);

        boolean result2 = customUserDetailsService.isEmail("andrew@gmailcom");
        Assertions.assertFalse(result2);

        boolean result3 = customUserDetailsService.isEmail("andrewgmail.com");
        Assertions.assertFalse(result3);

        boolean result4 = customUserDetailsService.isEmail("andrewgo");
        Assertions.assertFalse(result4);
    }
}