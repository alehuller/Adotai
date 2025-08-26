package br.com.alevhvm.adotai.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class TokenServiceTest {

    private TokenService tokenService;

    private final String secret = "superSecretKey123";
    private final long expireLength = 1000 * 60;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        tokenService = new TokenService();

        ReflectionTestUtils.setField(tokenService, "secret", secret);
        ReflectionTestUtils.setField(tokenService, "expireLength", expireLength);

        lenient().when(userDetails.getUsername()).thenReturn("usuarioTeste");
    }

    @Test
    void deveGerarTokenComSubjectCorreto() {
        String token = tokenService.generateToken(userDetails);

        assertNotNull(token);

        String subject = tokenService.validateToken(token);
        assertEquals("usuarioTeste", subject);
    }

    @Test
    void deveValidarTokenValidoERetornarSubject() {
        String token = tokenService.generateToken(userDetails);

        String subject = tokenService.validateToken(token);

        assertEquals("usuarioTeste", subject);
    }

    @Test
    void deveLancarExcecaoAoGerarTokenComSecretInvalida() {
        TokenService tokenServiceInvalido = new TokenService();
        ReflectionTestUtils.setField(tokenServiceInvalido, "secret", null);
        ReflectionTestUtils.setField(tokenServiceInvalido, "expireLength", expireLength);

        UserDetails usuarioFake = mock(UserDetails.class);

        assertThrows(RuntimeException.class, () -> tokenServiceInvalido.generateToken(usuarioFake));
    }

    @Test
    void deveLancarExcecaoAoValidarTokenExpirado() throws InterruptedException {
        TokenService tokenServiceExpirado = new TokenService();
        ReflectionTestUtils.setField(tokenServiceExpirado, "secret", secret);
        ReflectionTestUtils.setField(tokenServiceExpirado, "expireLength", 10L);

        String token = tokenServiceExpirado.generateToken(userDetails);

        Thread.sleep(20);

        assertThrows(RuntimeException.class, () -> tokenServiceExpirado.validateToken(token));
    }
}
