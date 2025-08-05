package br.com.alevhvm.adotai.administrador.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import br.com.alevhvm.adotai.administrador.model.Administrador;
import br.com.alevhvm.adotai.auth.enums.Roles;

//testes de repositorio exigem execucao real das queries, por isso esta sendo utilizado o @DataJpaTest
@DataJpaTest
@ActiveProfiles("test")
class AdministradorRepositoryTest {

    @Autowired
    private AdministradorRepository administradorRepository;

    private Administrador admin;

    @BeforeEach
    void setUp() {
        admin = new Administrador();
        admin.setEmail("adminteste@email.com");
        admin.setNome("AdminTeste");
        admin.setNomeUsuario("AdminTesteNome");
        admin.setSenha("senha123");
        admin.setCell("11999999999");
        admin.setRole(Roles.ADMIN);

        administradorRepository.save(admin);
    }

    @Test
    void deveEncontrarAdministradorComOsMesmosCamposSalvos() {
        Optional<Administrador> resultado = administradorRepository.findByEmail("adminteste@email.com");

        assertTrue(resultado.isPresent());
        Administrador adminSalvo = resultado.get();

        assertEquals("adminteste@email.com", adminSalvo.getEmail());
        assertEquals("AdminTeste", adminSalvo.getNome());
        assertEquals("AdminTesteNome", adminSalvo.getNomeUsuario());
        assertEquals("senha123", adminSalvo.getSenha());
        assertEquals("11999999999", adminSalvo.getCell());
        assertEquals(Roles.ADMIN, adminSalvo.getRole());
    }

    @Test
    void deveEncontrarAdministradorPorEmailQuandoHaEmailCadastrado() {
        Optional<Administrador> resultado = administradorRepository.findByEmail("adminteste@email.com");

        assertTrue(resultado.isPresent());
        assertEquals("AdminTesteNome", resultado.get().getNomeUsuario());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarAdministradorPorEmail() {
        assertThrows(NoSuchElementException.class, () -> {
            administradorRepository.findByEmail("adminemailerrado@email.com")
                .orElseThrow();
        });
    }

    @Test
    void deveEncontrarAdministradorPorNomeUsuario() {
        Optional<Administrador> resultado = administradorRepository.findByNomeUsuario("AdminTesteNome");

        assertTrue(resultado.isPresent());
        assertEquals("adminteste@email.com", resultado.get().getEmail());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarAdministradorPorNomeUsuario() {
        assertThrows(NoSuchElementException.class, () -> {
            administradorRepository.findByNomeUsuario("NomeUsuarioErrado")
                .orElseThrow();
        });
    }

    @Test
    void deveEncontrarAdminitradorPorCell() {
        Optional<Administrador> resultado = administradorRepository.findByCell("11999999999");

        assertTrue(resultado.isPresent());
        assertEquals("AdminTeste", resultado.get().getNome());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarAdministradorPorCell() {
        assertThrows(NoSuchElementException.class, () -> {
            administradorRepository.findByCell("11222222222")
                .orElseThrow();
        });
    }

    @Test
    void deveExcluirAdministradorPorNomeUsuario() {
        administradorRepository.deleteByNomeUsuario("AdminTesteNome");

        Optional<Administrador> resultado = administradorRepository.findByNomeUsuario("AdminTesteNome");

        assertTrue(resultado.isEmpty());
    }
}
