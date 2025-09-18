package br.com.alevhvm.adotai.usuario.validations;

import br.com.alevhvm.adotai.auth.dto.RegistroDTO;
import br.com.alevhvm.adotai.common.exceptions.ValidacaoException;
import br.com.alevhvm.adotai.common.vo.CpfVO;
import br.com.alevhvm.adotai.usuario.model.Usuario;
import br.com.alevhvm.adotai.usuario.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UsuarioValidacaoTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioValidacao validacao;

    private RegistroDTO registroDTO;

    private Usuario usuario;


    @BeforeEach
    void setUp(){
        registroDTO = new RegistroDTO();
        registroDTO.setEmail("teste@email.com");
        registroDTO.setCpf(new CpfVO("179.087.340-14"));
        registroDTO.setCell("(11) 91234-5678");
        registroDTO.setNomeUsuario("Testando Silva");

        usuario = new Usuario();
        usuario.setEmail("teste2@email.com");
        usuario.setCpf("488.432.080-80");
        usuario.setCell("(21) 99876-5432");
        usuario.setNomeUsuario("Testando Oliveira");
    }

    @Test
    void deveLancarExcecaoQuandoRegistroForNuloParaValidate(){
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            validacao.validate(null);
        });
        assertEquals("Não há dados", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExisteParaValidate(){
        when(usuarioRepository.findByEmail("teste@email.com"))
                .thenReturn(Optional.of(new Usuario()));

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
           validacao.validate(registroDTO);
        });

        assertTrue(ex.getErros().contains("E-mail já está em uso"));
    }

    @Test
    void deveLancarExcecaoQuandoCPFJaExisteParaValidate(){
        when(usuarioRepository.findByCpf("179.087.340-14"))
                .thenReturn(Optional.of(new Usuario()));

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            validacao.validate(registroDTO);
        });

        assertTrue(ex.getErros().contains("CPF já está em uso"));
    }

    @Test
    void deveLancarExcecaoQuandoCellJaExisteParaValidate(){
        when(usuarioRepository.findByCell("(11) 91234-5678"))
                .thenReturn(Optional.of(new Usuario()));

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            validacao.validate(registroDTO);
        });

        assertTrue(ex.getErros().contains("Cell já está em uso"));
    }

    @Test
    void deveLancarExcecaoQuandoNomeUsuarioJaExisteParaValidate(){
        when(usuarioRepository.findByNomeUsuario("Testando Silva"))
                .thenReturn(Optional.of(new Usuario()));

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            validacao.validate(registroDTO);
        });

        assertTrue(ex.getErros().contains("Nome de Usuário já está em uso"));
    }

    @Test
    void deveLancarMultiplasExcecoesQuandoMaisDeUmCampoJaExiste() {
        when(usuarioRepository.findByEmail("teste@email.com"))
                .thenReturn(Optional.of(new Usuario()));
        when(usuarioRepository.findByCpf("179.087.340-14"))
                .thenReturn(Optional.of(new Usuario()));
        when(usuarioRepository.findByCell("(11) 91234-5678"))
                .thenReturn(Optional.of(new Usuario()));
        when(usuarioRepository.findByNomeUsuario("Testando Silva"))
                .thenReturn(Optional.of(new Usuario()));

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            validacao.validate(registroDTO);
        });

        assertTrue(ex.getErros().contains("E-mail já está em uso"));
        assertTrue(ex.getErros().contains("CPF já está em uso"));
        assertTrue(ex.getErros().contains("Cell já está em uso"));
        assertTrue(ex.getErros().contains("Nome de Usuário já está em uso"));
    }

    @Test
    void naoDeveLancarExcecaoQuandoTodosDadosSaoValidosParaValidate() {
        when(usuarioRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(usuarioRepository.findByCpf(any())).thenReturn(Optional.empty());
        when(usuarioRepository.findByCell(any())).thenReturn(Optional.empty());
        when(usuarioRepository.findByNomeUsuario(any())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> validacao.validate(registroDTO));
    }

    @Test
    void naoDeveLancarExcecaoQuandoEmailNaoEstaEmUsoParaValidate() {
        when(usuarioRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> validacao.validate(registroDTO));
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExisteParaValidateUpdate() {
        when(usuarioRepository.findByEmail("teste2@email.com"))
                .thenReturn(Optional.of(new Usuario()));

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            validacao.validateUpdate(usuario);
        });

        assertTrue(ex.getErros().contains("E-mail já está em uso"));
    }

    @Test
    void deveLancarExcecaoQuandoCellJaExisteParaValidateUpdate() {
        when(usuarioRepository.findByCell("(21) 99876-5432"))
                .thenReturn(Optional.of(new Usuario()));

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            validacao.validateUpdate(usuario);
        });

        assertTrue(ex.getErros().contains("Cell já está em uso"));
    }

    @Test
    void deveLancarExcecaoQuandoNomeUsuarioJaExisteParaValidateUpdate() {
        when(usuarioRepository.findByNomeUsuario("Testando Oliveira"))
                .thenReturn(Optional.of(new Usuario()));

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            validacao.validateUpdate(usuario);
        });
        assertTrue(ex.getErros().contains("Nome de Usuário já está em uso"));
    }

    @Test
    void naoDeveLancarExcecaoQuandoTodosDadosSaoValidosParaValidateUpdate() {
        when(usuarioRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(usuarioRepository.findByNomeUsuario(any())).thenReturn(Optional.empty());
        when(usuarioRepository.findByCell(any())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> validacao.validateUpdate(usuario));
    }

    @Test
    void naoDeveLancarExcecaoQuandoEmailNaoEstaEmUsoParaValidadeUpdate() {
        when(usuarioRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> validacao.validateUpdate(usuario));
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExisteParaPartialUpdate() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setNomeUsuario("outroUsuario");

        when(usuarioRepository.findByEmail("teste@email.com"))
                .thenReturn(Optional.of(usuarioExistente));

        Map<String, Object> updates = new HashMap<>();
        updates.put("email", "teste@email.com");

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            validacao.validatePartialUpdate("usuarioteste", updates);
        });

        assertTrue(ex.getErros().contains("E-mail já está em uso por outro usuário"));
    }

    @Test
    void deveLancarExcecaoQuandoNomeUsuarioJaExisteParaPartialUpdate() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setNomeUsuario("testeExistente");

        when(usuarioRepository.findByNomeUsuario("testeExistente"))
                .thenReturn(Optional.of(usuarioExistente));

        Map<String, Object> updates = new HashMap<>();
        updates.put("nomeUsuario", "testeExistente");

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            validacao.validatePartialUpdate("usuarioteste", updates);
        });

        assertTrue(ex.getErros().contains("Nome de usuário já está em uso por outro usuário"));
    }

    @Test
    void deveLancarExcecaoQuandoCelularJaExisteParaPartialUpdate() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setNomeUsuario("testeExistente");

        when(usuarioRepository.findByCell("11333333333"))
                .thenReturn(Optional.of(usuarioExistente));

        Map<String, Object> updates = new HashMap<>();
        updates.put("cell", "11333333333");

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            validacao.validatePartialUpdate("usuarioteste", updates);
        });

        assertTrue(ex.getErros().contains("Celular já está em uso por outro usuário"));
    }

    @Test
    void naoDeveLancarExcecaoQuandoUsuarioAtualizarNomeComProprioNome() {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setNomeUsuario("ProprioNome");

        when(usuarioRepository.findByNomeUsuario("ProprioNome"))
                .thenReturn(Optional.of(usuarioExistente));

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

        when(usuarioRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(usuarioRepository.findByNomeUsuario(any())).thenReturn(Optional.empty());
        when(usuarioRepository.findByCell(any())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> validacao.validatePartialUpdate("usuarioteste", updates));
    }

    @Test
    void naoDeveValidarCampoNaoPresenteNoPartialUpdate() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("nomeUsuario", "NovoNome");

        when(usuarioRepository.findByNomeUsuario("NovoNome"))
                .thenReturn(Optional.empty());

        assertDoesNotThrow(() -> validacao.validatePartialUpdate("usuarioteste", updates));
    }
}
