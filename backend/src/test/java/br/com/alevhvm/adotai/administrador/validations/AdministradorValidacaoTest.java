package br.com.alevhvm.adotai.administrador.validations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alevhvm.adotai.administrador.dto.AdministradorDTO;
import br.com.alevhvm.adotai.administrador.model.Administrador;
import br.com.alevhvm.adotai.administrador.repository.AdministradorRepository;
import br.com.alevhvm.adotai.common.exceptions.ValidacaoException;

@ExtendWith(MockitoExtension.class)
class AdministradorValidacaoTest {

    @Mock
    private AdministradorRepository administradorRepository;

    @InjectMocks
    private AdministradorValidacao validacao;

    private AdministradorDTO adminDTO;

    private Administrador admin;

    @BeforeEach
    void setUp() {
        adminDTO = new AdministradorDTO();
        adminDTO.setEmail("teste@email.com");
        adminDTO.setNomeUsuario("usuario123");
        adminDTO.setCell("11999999999");

        admin = new Administrador();
        admin.setEmail("testeadmin@email.com");
        admin.setNomeUsuario("usuarioteste");
        admin.setCell("11222222222");
    }

    @Test
    void deveLancarExcecaoQuandoAdminEstiverNuloNoValidate() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            validacao.validate(null);
        });

        assertEquals("Não há dados", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExisteParaValidate() {
        when(administradorRepository.findByEmail("teste@email.com"))
                .thenReturn(Optional.of(new Administrador()));

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            validacao.validate(adminDTO);
        });

        assertTrue(ex.getErros().contains("E-mail já está em uso"));
    }

    @Test
    void deveLancarExcecaoQuandoNomeUsuarioJaExisteParaValidate() {
        when(administradorRepository.findByNomeUsuario("usuario123"))
                .thenReturn(Optional.of(new Administrador()));

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            validacao.validate(adminDTO);
        });

        assertTrue(ex.getErros().contains("Nome de Usuário já está em uso"));
    }

    @Test
    void deveLancarExcecaoQuandoCelularJaExisteParaValidate() {
        when(administradorRepository.findByCell("11999999999"))
                .thenReturn(Optional.of(new Administrador()));

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            validacao.validate(adminDTO);
        });

        assertTrue(ex.getErros().contains("Cell já está em uso"));
    }

    @Test
    void deveLancarMultiplasExcecoesQuandoMaisDeUmCampoJaExiste() {
        when(administradorRepository.findByEmail("teste@email.com"))
            .thenReturn(Optional.of(new Administrador()));
        when(administradorRepository.findByNomeUsuario("usuario123"))
            .thenReturn(Optional.of(new Administrador()));

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            validacao.validate(adminDTO);
        });

        assertTrue(ex.getErros().contains("E-mail já está em uso"));
        assertTrue(ex.getErros().contains("Nome de Usuário já está em uso"));
    }

    @Test
    void naoDeveLancarExcecaoQuandoTodosDadosSaoValidosParaValidate() {
        when(administradorRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(administradorRepository.findByNomeUsuario(any())).thenReturn(Optional.empty());
        when(administradorRepository.findByCell(any())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> validacao.validate(adminDTO));
    }

    @Test
    void naoDeveLancarExcecaoQuandoEmailNaoEstaEmUsoParaValidate() {
        when(administradorRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> validacao.validate(adminDTO));
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExisteParaValidateUpdate() {
        when(administradorRepository.findByEmail("testeadmin@email.com"))
                .thenReturn(Optional.of(new Administrador()));

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            validacao.validateUpdate(admin);
        });

        assertTrue(ex.getErros().contains("E-mail já está em uso"));
    }

    @Test
    void deveLancarExcecaoQuandoNomeUsuarioJaExisteParaValidateUpdate() {
        when(administradorRepository.findByNomeUsuario("usuarioteste"))
                .thenReturn(Optional.of(new Administrador()));

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            validacao.validateUpdate(admin);
        });

        assertTrue(ex.getErros().contains("Nome de Usuário já está em uso"));
    }

    @Test
    void deveLancarExcecaoQuandoCelularJaExisteParaValidateUpdate() {
        when(administradorRepository.findByCell("11222222222"))
                .thenReturn(Optional.of(new Administrador()));

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            validacao.validateUpdate(admin);
        });

        assertTrue(ex.getErros().contains("Cell já está em uso"));
    }

    @Test
    void naoDeveLancarExcecaoQuandoTodosDadosSaoValidosParaValidateUpdate() {
        when(administradorRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(administradorRepository.findByNomeUsuario(any())).thenReturn(Optional.empty());
        when(administradorRepository.findByCell(any())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> validacao.validateUpdate(admin));
    }

    @Test
    void naoDeveLancarExcecaoQuandoEmailNaoEstaEmUsoParaValidadeUpdate() {
        when(administradorRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> validacao.validateUpdate(admin));
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExisteParaPartialUpdate() {
        Administrador adminExistente = new Administrador();
        adminExistente.setNomeUsuario("outroAdmin");

        when(administradorRepository.findByEmail("teste@email.com"))
                .thenReturn(Optional.of(adminExistente));

        Map<String, Object> updates = new HashMap<>();
        updates.put("email", "teste@email.com");

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            validacao.validatePartialUpdate("usuarioteste", updates);
        });

        assertTrue(ex.getErros().contains("E-mail já está em uso por outro administrador"));
    }

    @Test
    void deveLancarExcecaoQuandoNomeUsuarioJaExisteParaPartialUpdate() {
        Administrador adminExistente = new Administrador();
        adminExistente.setNomeUsuario("testeExistente");

        when(administradorRepository.findByNomeUsuario("testeExistente"))
                .thenReturn(Optional.of(adminExistente));

        Map<String, Object> updates = new HashMap<>();
        updates.put("nomeUsuario", "testeExistente");

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            validacao.validatePartialUpdate("usuarioteste", updates);
        });

        assertTrue(ex.getErros().contains("Nome de usuário já está em uso por outro administrador"));
    }

    @Test
    void deveLancarExcecaoQuandoCelularJaExisteParaPartialUpdate() {
        Administrador adminExistente = new Administrador();
        adminExistente.setNomeUsuario("testeExistente");

        when(administradorRepository.findByCell("11333333333"))
                .thenReturn(Optional.of(adminExistente));

        Map<String, Object> updates = new HashMap<>();
        updates.put("cell", "11333333333");

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            validacao.validatePartialUpdate("usuarioteste", updates);
        });

        assertTrue(ex.getErros().contains("Celular já está em uso por outro administrador"));
    }

    @Test
    void naoDeveLancarExcecaoQuandoAdminAtualizarNomeComProprioNome() {
        Administrador adminExistente = new Administrador();
        adminExistente.setNomeUsuario("ProprioNome");

        when(administradorRepository.findByNomeUsuario("ProprioNome"))
                .thenReturn(Optional.of(adminExistente));

        Map<String, Object> updates = new HashMap<>();
        updates.put("nomeUsuario", "ProprioNome");

        assertDoesNotThrow(() -> validacao.validatePartialUpdate("ProprioNome", updates));
    }

    @Test
    void naoDeveLancarExcecaoQuandoTodosDadosSaoValidosParaPartialUpdate() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("email", "emailnaousado@email.com");
        updates.put("nomeUsuario", "NomeNaoUsado");
        updates.put("cell", "11777777777");

        when(administradorRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(administradorRepository.findByNomeUsuario(any())).thenReturn(Optional.empty());
        when(administradorRepository.findByCell(any())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> validacao.validatePartialUpdate("usuarioteste", updates));
    }

    @Test
    void naoDeveValidarCampoNaoPresenteNoPartialUpdate() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("nomeUsuario", "NovoNome");

        when(administradorRepository.findByNomeUsuario("NovoNome"))
            .thenReturn(Optional.empty());

        assertDoesNotThrow(() -> validacao.validatePartialUpdate("usuarioteste", updates));
    }

}

