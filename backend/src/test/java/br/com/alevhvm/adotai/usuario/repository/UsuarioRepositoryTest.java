package br.com.alevhvm.adotai.usuario.repository;

import br.com.alevhvm.adotai.administrador.model.Administrador;
import br.com.alevhvm.adotai.animal.enums.StatusAnimal;
import br.com.alevhvm.adotai.animal.model.Animal;
import br.com.alevhvm.adotai.auth.enums.Roles;
import br.com.alevhvm.adotai.common.vo.DescricaoVO;
import br.com.alevhvm.adotai.ong.model.Ong;
import br.com.alevhvm.adotai.usuario.model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

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

        Set<Animal> animaisFavoritos = new HashSet<>();
        Animal animal1 = new Animal();

        usuarioRepository.save(usuario);
    }

    @Test
    void deveEncontrarUsuarioComOsMesmosCamposSalvos() {
        Optional<Usuario> resultado = usuarioRepository.findByEmail("teste@email.com");

        assertTrue(resultado.isPresent());
        Usuario userSalvo = resultado.get();

        assertEquals(usuario.getEmail(), userSalvo.getEmail());
        assertEquals(usuario.getNome(), userSalvo.getNome());
        assertEquals(usuario.getNomeUsuario(), userSalvo.getNomeUsuario());
        assertEquals(usuario.getSenha(), userSalvo.getSenha());
        assertEquals(usuario.getCell(), userSalvo.getCell());
        assertEquals(usuario.getCpf(), userSalvo.getCpf());
        assertEquals(Roles.USER, userSalvo.getRole());
    }

    @Test
    void deveEncontrarUsuarioPorEmail() {
        Optional<Usuario> resultado = usuarioRepository.findByEmail("teste@email.com");

        assertTrue(resultado.isPresent());
        assertEquals("UserTesteNome", resultado.get().getNomeUsuario());
    }

    @Test
    void deveEncontrarUsuarioPorNomeUsuario() {
        Optional<Usuario> resultado = usuarioRepository.findByNomeUsuario("UserTesteNome");

        assertTrue(resultado.isPresent());
        assertEquals("teste@email.com", resultado.get().getEmail());
    }

    @Test
    void deveEncontrarUsuarioPorCell() {
        Optional<Usuario> resultado = usuarioRepository.findByCell("11999999999");

        assertTrue(resultado.isPresent());
        assertEquals(usuario.getCell(), resultado.get().getCell());
    }

    @Test
    void deveEncontrarUsuarioPorCpf() {
        Optional<Usuario> resultado = usuarioRepository.findByCpf("824.387.720-77");

        assertTrue(resultado.isPresent());
        assertEquals(usuario.getCpf(), resultado.get().getCpf());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarUsuarioPorEmail() {
        assertThrows(NoSuchElementException.class, () -> {
            usuarioRepository.findByEmail("emailerrado@email.com")
                    .orElseThrow();
        });
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarUsuarioPorNomeUsuario() {
        assertThrows(NoSuchElementException.class, () -> {
            usuarioRepository.findByNomeUsuario("NomeUsuarioErrado")
                    .orElseThrow();
        });
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarUsuarioPorCell() {
        assertThrows(NoSuchElementException.class, () -> {
            usuarioRepository.findByCell("11222222222")
                    .orElseThrow();
        });
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarUsuarioPorCpf() {
        assertThrows(NoSuchElementException.class, () -> {
            usuarioRepository.findByCell("11222222222")
                    .orElseThrow();
        });
    }

    @Test
    void deveExcluirUsuarioPorNomeUsuario() {
        usuarioRepository.deleteByNomeUsuario(usuario.getNomeUsuario());

        Optional<Usuario> resultado = usuarioRepository.findByNomeUsuario("AdminTesteNome");

        assertTrue(resultado.isEmpty());
    }

    @Test
    void deveEncontrarAnimaisFavoritosPorNomeUsuario() {

        Pageable pageable = PageRequest.of(0, 10);
        Page<Animal> resultado = usuarioRepository.findAnimaisFavoritosByNomeUsuario("UserTesteNome", pageable);

        assertFalse(resultado.isEmpty());
        assertEquals(usuario.getNomeUsuario(), resultado.getContent().get().ge);
    }
}
