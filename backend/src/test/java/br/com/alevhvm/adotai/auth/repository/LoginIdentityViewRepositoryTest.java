package br.com.alevhvm.adotai.auth.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.alevhvm.adotai.auth.enums.Roles;
import br.com.alevhvm.adotai.auth.model.LoginIdentityView;
import br.com.alevhvm.adotai.usuario.model.Usuario;
import br.com.alevhvm.adotai.usuario.repository.UsuarioRepository;

@DataJpaTest
@ActiveProfiles("test")
public class LoginIdentityViewRepositoryTest {

    @Autowired
    private LoginIdentityViewRepository loginIdentityViewRepository;

    @Autowired
    private UsuarioRepository usuarioRepository; 

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setEmail("teste@email.com");
        usuario.setNome("UserTeste");
        usuario.setNomeUsuario("UserTesteNome");
        usuario.setSenha("senha123");
        usuario.setCell("11999999999");
        usuario.setCpf("824.387.720-77");
        usuario.setRole(Roles.USER);
        usuarioRepository.save(usuario);
    }

    @Test
    void deveEncontrarUsuarioQuandoProcuradoPorEmailExistente() {
        Optional<LoginIdentityView> resultado = loginIdentityViewRepository.findByEmail("teste@email.com");

        assertTrue(resultado.isPresent());
        assertEquals("teste@email.com", resultado.get().getEmail());
    }
}
