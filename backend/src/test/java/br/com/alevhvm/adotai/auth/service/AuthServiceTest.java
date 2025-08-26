package br.com.alevhvm.adotai.auth.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import br.com.alevhvm.adotai.auth.model.LoginIdentityView;
import br.com.alevhvm.adotai.auth.repository.LoginIdentityViewRepository;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private LoginIdentityViewRepository loginIdentityViewRepository;

    private LoginIdentityView usuario;

    @BeforeEach
    void setUp() {
        usuario = new LoginIdentityView();
        ReflectionTestUtils.setField(usuario, "email", "teste@email.com");
        ReflectionTestUtils.setField(usuario, "senha", "senha");
        ReflectionTestUtils.setField(usuario, "nomeUsuario", "testeUser");
    }


    @Test
    void deveRetornarUsuarioQuandoEncontradoPorEmail() {
        when(loginIdentityViewRepository.findByEmail("teste@email.com")).thenReturn(Optional.of(usuario));

        UserDetails resultado = authService.loadUserByUsername("teste@email.com");

        assertNotNull(resultado);
        assertEquals("teste@email.com", resultado.getUsername());
        assertEquals("senha", resultado.getPassword());
    }

    @Test
    void deveRetornarUsuarioQuandoEncontradoPorNomeUsuario() {
        when(loginIdentityViewRepository.findByEmail("testeUser")).thenReturn(Optional.empty());
        when(loginIdentityViewRepository.findByNomeUsuario("testeUser")).thenReturn(Optional.of(usuario));

        UserDetails resultado = authService.loadUserByUsername("testeUser");

        assertNotNull(resultado);
        assertEquals("teste@email.com", resultado.getUsername());
        assertEquals("senha", resultado.getPassword());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarUsuarioNemPorEmailNemPorNomeUsuario() {
        when(loginIdentityViewRepository.findByEmail("naoexiste")).thenReturn(Optional.empty());
        when(loginIdentityViewRepository.findByNomeUsuario("naoexiste")).thenReturn(Optional.empty());

        UsernameNotFoundException ex = assertThrows(UsernameNotFoundException.class, () -> {
            authService.loadUserByUsername("naoexiste");
        });

        assertEquals("Usuário ou ONG não encontrado com o identificador: " + "naoexiste", ex.getMessage());
    }
}
