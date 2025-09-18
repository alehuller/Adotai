package br.com.alevhvm.adotai.common.runner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.alevhvm.adotai.administrador.repository.AdministradorRepository;
import br.com.alevhvm.adotai.ong.repository.OngRepository;
import br.com.alevhvm.adotai.usuario.model.Usuario;
import br.com.alevhvm.adotai.usuario.repository.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
public class PasswordEncoderRunnerTest {
    
    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private OngRepository ongRepository;

    @Mock
    private AdministradorRepository administradorRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PasswordEncoderRunner runner;

    @Test
    void deveCodificarSenhaNaoCodificada() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setSenha("senha123");

        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));
        when(ongRepository.findAll()).thenReturn(List.of());
        when(administradorRepository.findAll()).thenReturn(List.of());
        when(passwordEncoder.encode("senha123")).thenReturn("{bcrypt}senhaCriptografada");

        runner.run(null);

        assertEquals("{bcrypt}senhaCriptografada", usuario.getSenha());
    }

    @Test
    void naoDeveCodificarSenhaJaCodificada() throws Exception {
        Usuario usuario = new Usuario();
        usuario.setSenha("{bcrypt}senhaCriptografada");

        when(usuarioRepository.findAll()).thenReturn(List.of(usuario));
        when(ongRepository.findAll()).thenReturn(List.of());
        when(administradorRepository.findAll()).thenReturn(List.of());

        runner.run(null);

        assertEquals("{bcrypt}senhaCriptografada", usuario.getSenha());
        verify(passwordEncoder, never()).encode(any());
    }
}
