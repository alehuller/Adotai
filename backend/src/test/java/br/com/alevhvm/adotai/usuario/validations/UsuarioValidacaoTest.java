package br.com.alevhvm.adotai.usuario.validations;

import br.com.alevhvm.adotai.administrador.model.Administrador;
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


}
