package br.com.alevhvm.adotai.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.http.HttpServletRequest;

@ExtendWith(MockitoExtension.class)
public class TokenBlackListServiceTest {

    @InjectMocks
    private TokenBlackListService tokenBlackListService;

    @Mock
    private HttpServletRequest httpServletRequest;

    private final String token = "meuToken123";
    private final String bearerToken = "Bearer " + token;

    @BeforeEach
    void setUp() {
    }

    @Test
    void deveAdicionarTokenABlackList() {
        when(httpServletRequest.getHeader("Authorization")).thenReturn(bearerToken);

        tokenBlackListService.addToBlacklist(httpServletRequest);

        assertTrue(tokenBlackListService.isTokenBlacklisted(token));
    }

    @Test
    void deveRetornarFalseQuandoTokenNaoEstaNaBlacklist() {
        assertFalse(tokenBlackListService.isTokenBlacklisted("tokenInexistente"));
    }

    @Test
    void deveLancarExcecaoQuandoAuthorizationHeaderEstiverNulo() {
        when(httpServletRequest.getHeader("Authorization")).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            tokenBlackListService.addToBlacklist(httpServletRequest);
        });

        assertEquals("Token não encontrado na requisição", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoAuthorizationHeaderNaoComecaComBearer() {
        when(httpServletRequest.getHeader("Authorization")).thenReturn("Token " + token);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            tokenBlackListService.addToBlacklist(httpServletRequest);
        });

        assertEquals("Token não encontrado na requisição", ex.getMessage());
    }
}
