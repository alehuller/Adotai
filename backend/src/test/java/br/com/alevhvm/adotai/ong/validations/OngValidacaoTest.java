package br.com.alevhvm.adotai.ong.validations;

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

import br.com.alevhvm.adotai.common.exceptions.ValidacaoException;
import br.com.alevhvm.adotai.common.vo.CnpjVO;
import br.com.alevhvm.adotai.common.vo.EnderecoVO;
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

    private Ong ong;

    @BeforeEach
    void setUp() {
        ongDTO = new OngDTO();
        ongDTO.setEmail("testeong@email.com");
        ongDTO.setCnpj(new CnpjVO("86.298.529/0001-59"));
        ongDTO.setCell("11555555555");
        ongDTO.setNomeUsuario("TesteOng");

        ong = new Ong();
        ong.setEmail("teste@email.com");
        ong.setCnpj("40.972.445/0001-58");
        ong.setCell("11666666666");
        ong.setNomeUsuario("TesteOng2");
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

    @Test
    void naoDeveLancarExcecaoQuandoEmailNaoEstaEmUsoParaValidate() {
        when(ongRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> ongValidacao.validate(ongDTO));
    }

    @Test
    void deveLancarExcecaoQuandoOngEstiverNuloNoValidate() {
        NullPointerException ex = assertThrows(NullPointerException.class, () -> {
            ongValidacao.validate(null);
        });

        assertEquals("Não há dados", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExisteParaValidateUpdate() {
        when(ongRepository.findByEmail("teste@email.com"))
                .thenReturn(Optional.of(new Ong()));

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            ongValidacao.validateUpdate(ong);
        });

        assertTrue(ex.getErros().contains("E-mail já está em uso"));
    }

    @Test
    void deveLancarExcecaoQuandoNomeUsuarioJaExisteParaValidateUpdate() {
        when(ongRepository.findByNomeUsuario("TesteOng2"))
                .thenReturn(Optional.of(new Ong()));

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            ongValidacao.validateUpdate(ong);
        });

        assertTrue(ex.getErros().contains("Nome Usuário já está em uso"));
    }

    @Test
    void deveLancarExcecaoQuandoCelularJaExisteParaValidateUpdate() {
        when(ongRepository.findByCell("11666666666"))
                .thenReturn(Optional.of(new Ong()));

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            ongValidacao.validateUpdate(ong);
        });

        assertTrue(ex.getErros().contains("Celular já está em uso"));
    }

    @Test
    void naoDeveLancarExcecaoQuandoTodosDadosSaoValidosParaValidadeUpdate() {
        when(ongRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(ongRepository.findByNomeUsuario(any())).thenReturn(Optional.empty());
        when(ongRepository.findByCell(any())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> ongValidacao.validateUpdate(ong));
    }

    @Test
    void naoDeveLancarExcecaoQuandoEmailNaoEstaEmUsoParaValidadeUpdate() {
        when(ongRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> ongValidacao.validateUpdate(ong));
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaExisteParaPartialUpdate() {
        Ong ongExistente = new Ong();
        ongExistente.setNomeUsuario("outraOng");

        when(ongRepository.findByEmail("testeong@email.com"))
                .thenReturn(Optional.of(ongExistente));
        
        Map<String, Object> updates = new HashMap<>();
        updates.put("email", "testeong@email.com");

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            ongValidacao.validatePartialUpdate("TesteOng", updates);
        });

        assertTrue(ex.getErros().contains("E-mail já está em uso por outra ong"));
    }

    @Test
    void deveLancarExcecaoQuandoNomeUsuarioJaExisteParaPartialUpdate() {
        Ong ongExistente = new Ong();
        ongExistente.setNomeUsuario("nomeExistente");

        when(ongRepository.findByNomeUsuario("nomeExistente"))
                .thenReturn(Optional.of(ongExistente));

        Map<String, Object> updates = new HashMap<>();
        updates.put("nomeUsuario", "nomeExistente");

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            ongValidacao.validatePartialUpdate("ongTeste", updates);
        });

        assertTrue(ex.getErros().contains("Nome de usuário já está em uso por outra ong"));
    }

    @Test
    void deveLancarExcecaoQuandoCelularJaExisteParaPartialUpdate() {
        Ong ongExistente = new Ong();
        ongExistente.setNomeUsuario("nomeExistente");

        when(ongRepository.findByCell("11111111111"))
                .thenReturn(Optional.of(ongExistente));

        Map<String, Object> updates = new HashMap<>();
        updates.put("cell", "11111111111");

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            ongValidacao.validatePartialUpdate("ongTeste", updates);
        });

        assertTrue(ex.getErros().contains("Celular já está em uso por outra ong"));
    }

    @Test
    void deveLancarExcecaoQuandoOngTentaAtualizarCnpjParaPartialUpdate() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("cnpj", "56.353.505/0001-94");

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            ongValidacao.validatePartialUpdate("cnpjTeste", updates);
        });

        assertTrue(ex.getErros().contains("Não é permitido alterar o CNPJ."));
    }

    @Test
    void naoDeveLancarExcecaoQuandoOngAtualizarNomeComProprioNome() {
        Ong ongExistente = new Ong();
        ongExistente.setNomeUsuario("ProprioNome");

        when(ongRepository.findByNomeUsuario("ProprioNome"))
                .thenReturn(Optional.of(ongExistente));

        Map<String, Object> updates = new HashMap<>();
        updates.put("nomeUsuario", "ProprioNome");

        assertDoesNotThrow(() -> ongValidacao.validatePartialUpdate("ProprioNome", updates));
    }

    @Test
    void naoDeveLancarExcecaoQuandoTodosDadosSaoValidosParaPartialUpdate() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("email", "emailnaousado@email.com");
        updates.put("nomeUsuario", "NomeNaoUsado");
        updates.put("cell", "11777777777");

        when(ongRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(ongRepository.findByNomeUsuario(any())).thenReturn(Optional.empty());
        when(ongRepository.findByCell(any())).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> ongValidacao.validatePartialUpdate("usuarioteste", updates));
    }

    @Test
    void naoDeveValidarCampoNaoPresenteNoPartialUpdate() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("nomeUsuario", "NovoNome");

        when(ongRepository.findByNomeUsuario("NovoNome"))
            .thenReturn(Optional.empty());

        assertDoesNotThrow(() -> ongValidacao.validatePartialUpdate("usuarioteste", updates));
    }

    @Test
    void deveLancarExcecaoQuandoLogradouroEstaNulo() {
        EnderecoVO endereco = new EnderecoVO();
        endereco.setLogradouro(null);

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            ongValidacao.validarEnderecoPreenchido(endereco);
        });

        assertTrue(ex.getErros().contains("Logradouro não encontrado para o CEP informado"));
    }

    @Test
    void deveLancarExcecaoQuandoLogradouroEstaEmBranco() {
        EnderecoVO endereco = new EnderecoVO();
        endereco.setLogradouro(" ");

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            ongValidacao.validarEnderecoPreenchido(endereco);
        });

        assertTrue(ex.getErros().contains("Logradouro não encontrado para o CEP informado"));
    }

    @Test
    void deveLancarExcecaoQuandoBairroEstaNulo() {
        EnderecoVO endereco = new EnderecoVO();
        endereco.setBairro(null);

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            ongValidacao.validarEnderecoPreenchido(endereco);
        });

        assertTrue(ex.getErros().contains("Bairro não encontrado para o CEP informado"));
    }

    @Test
    void deveLancarExcecaoQuandoBairroEstaEmBranco() {
        EnderecoVO endereco = new EnderecoVO();
        endereco.setBairro(" ");

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            ongValidacao.validarEnderecoPreenchido(endereco);
        });

        assertTrue(ex.getErros().contains("Bairro não encontrado para o CEP informado"));
    }

    @Test
    void deveLancarExcecaoQuandoCidadeEstaNulo() {
        EnderecoVO endereco = new EnderecoVO();
        endereco.setCidade(null);

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            ongValidacao.validarEnderecoPreenchido(endereco);
        });

        assertTrue(ex.getErros().contains("Cidade não encontrada para o CEP informado"));
    }

    @Test
    void deveLancarExcecaoQuandoCidadeEstaEmBranco() {
        EnderecoVO endereco = new EnderecoVO();
        endereco.setCidade(" ");

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            ongValidacao.validarEnderecoPreenchido(endereco);
        });

        assertTrue(ex.getErros().contains("Cidade não encontrada para o CEP informado"));
    }

    @Test
    void deveLancarExcecaoQuandoEstadoEstaNulo() {
        EnderecoVO endereco = new EnderecoVO();
        endereco.setEstado(null);

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            ongValidacao.validarEnderecoPreenchido(endereco);
        });

        assertTrue(ex.getErros().contains("Estado não encontrado para o CEP informado"));
    }

    @Test
    void deveLancarExcecaoQuandoEstadoEstaEmBranco() {
        EnderecoVO endereco = new EnderecoVO();
        endereco.setEstado(" ");

        ValidacaoException ex = assertThrows(ValidacaoException.class, () -> {
            ongValidacao.validarEnderecoPreenchido(endereco);
        });

        assertTrue(ex.getErros().contains("Estado não encontrado para o CEP informado"));
    }

    @Test
    void naoDeveLancarExcecaoQuandoEnderecoCompletoEstiverValido() {
        EnderecoVO endereco = new EnderecoVO();
        endereco.setLogradouro("Rua TESTE");
        endereco.setBairro("Bairro TESTE");
        endereco.setCidade("Cidade TESTE");
        endereco.setEstado("Estado TESTE");

        assertDoesNotThrow(() -> ongValidacao.validarEnderecoPreenchido(endereco));
    }
}
