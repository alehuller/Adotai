package br.com.alevhvm.adotai.ong.validations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.alevhvm.adotai.common.exceptions.ValidacaoException;
import br.com.alevhvm.adotai.common.vo.CnpjVO;
import br.com.alevhvm.adotai.ong.dto.OngDTO;
import br.com.alevhvm.adotai.ong.model.Ong;
import br.com.alevhvm.adotai.ong.repository.OngRepository;

@ExtendWith(MockitoExtension.class)
class OngValidacaoTest {

    @Mock
    private OngRepository ongRepository;

    @InjectMocks
    private OngValidacao ongValidacao;

    private OngDTO ongDTO;

    @BeforeEach
    void setUp() {
        ongDTO = new OngDTO();
        ongDTO.setEmail("testeong@email.com");
        ongDTO.setCnpj(new CnpjVO("86.298.529/0001-59"));
        ongDTO.setCell("11555555555");
        ongDTO.setNomeUsuario("TesteOng");
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExisteParaValidate() {
        when(ongRepository.findByEmail("testeong@email.com"))
                .thenReturn(Optional.of(new Ong()));

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            ongValidacao.validate(ongDTO);
        });

        assertTrue(ex.getErros().contains("E-mail já está em uso"));
    }

    @Test
    void deveLancarExcecaoQuandoNomeUsuarioJaExisteParaValidate() {
        when(ongRepository.findByNomeUsuario("TesteOng"))
                .thenReturn(Optional.of(new Ong()));

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            ongValidacao.validate(ongDTO);
        });

        assertTrue(ex.getErros().contains("Nome Usuário já está em uso"));
    }

    @Test
    void deveLancarExcecaoQuandoCelularJaExisteParaValidate() {
        when(ongRepository.findByCell("11555555555"))
                .thenReturn(Optional.of(new Ong()));

        ValidacaoException ex = assertThrows(ValidacaoException.class, () ->{
            ongValidacao.validate(ongDTO);
        });

        assertTrue(ex.getErros().contains("Celular já está em uso"));
    }

    @Test
    void deveLancarExcecaoQuandoCnpjJaExisteParaValidate() {
        when(ongRepository.findByCnpj("86.298.529/0001-59"))
                .thenReturn(Optional.of(new Ong()));

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            ongValidacao.validate(ongDTO);
        });

        assertTrue(ex.getErros().contains("CNPJ já está em uso"));
    }

    @Test
    void deveLancarMultiplasExcecoesQuandoMaisDeUmCampoJaExiste() {
        when(ongRepository.findByCell("11555555555"))
                .thenReturn(Optional.of(new Ong()));
        when(ongRepository.findByCnpj("86.298.529/0001-59"))
                .thenReturn(Optional.of(new Ong()));

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            ongValidacao.validate(ongDTO);
        });

        assertTrue(ex.getErros().contains("Celular já está em uso"));
        assertTrue(ex.getErros().contains("CNPJ já está em uso"));
    }

    @Test
    void naoDeveLancarExcecaoQuandoTodosDadosSaoValidosParaValidate() {
        when(ongRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(ongRepository.findByNomeUsuario(any())).thenReturn(Optional.empty());
        when(ongRepository.findByCell(any())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> ongValidacao.validate(ongDTO));
    }
}
