package br.com.alevhvm.adotai.ong.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.alevhvm.adotai.auth.enums.Roles;
import br.com.alevhvm.adotai.common.exceptions.ValidacaoException;
import br.com.alevhvm.adotai.common.vo.CnpjVO;
import br.com.alevhvm.adotai.common.vo.EnderecoVO;
import br.com.alevhvm.adotai.common.vo.RedeVO;
import br.com.alevhvm.adotai.ong.dto.OngDTO;
import br.com.alevhvm.adotai.ong.model.Ong;
import br.com.alevhvm.adotai.ong.repository.OngRepository;
import br.com.alevhvm.adotai.ong.validations.OngValidacao;
import br.com.alevhvm.adotai.usuario.dto.UsuarioDTO;
import br.com.alevhvm.adotai.usuario.model.Usuario;

@ExtendWith(MockitoExtension.class)
public class OngServiceTest {

    @InjectMocks
    private OngService ongService;

    @Mock
    private CepService cepService;

    @Mock
    private OngRepository ongRepository;

    @Mock
    private OngValidacao ongValidacao;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Ong ong;
    private OngDTO ongDTO;

    private RedeVO redeVO;
    private List<String> erros;

    @BeforeEach
    void setUp() {
        redeVO = new RedeVO();
        redeVO.setInstagram("https://teste.adotai.com/");
        redeVO.setFacebook("https://teste.facebook.com/adotai");

        ong = new Ong();
        ong.setIdOng(1L);
        ong.setNome("Amigos dos Animais");
        ong.setNomeUsuario("amigosanimais");
        ong.setEmail("contato@amigosanimais.org");
        ong.setSenha("senhaCriptografada");
        ong.setFotoPerfil("foto_ong.png");
        ong.setCell("11988887777");
        ong.setRole(Roles.ONG);
        ong.setEndereco(new EnderecoVO(
                "Rua das Flores",
                "123",
                "Casa 2",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000"
        ));
        ong.setCnpj("12.345.678/0001-90");
        ong.setResponsavel("Maria Silva");
        ong.setDescricao("ONG dedicada ao resgate e adoção de animais abandonados.");
        ong.setRede(redeVO);

        ongDTO = new OngDTO();
        ongDTO.setKey(1L);
        ongDTO.setNome("Amigos dos Animais");
        ongDTO.setNomeUsuario("amigosanimais");
        ongDTO.setFotoPerfil("foto_ong.png");
        ongDTO.setEmail("contato@amigosanimais.org");
        ongDTO.setSenha("123456");
        ongDTO.setEndereco(new EnderecoVO(
                "Rua das Flores",
                "123",
                "Casa 2",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000"
        ));
        ongDTO.setCell("11988887777");
        ongDTO.setCnpj(new CnpjVO("12.345.678/0001-90"));
        ongDTO.setResponsavel("Maria Silva");
        ongDTO.setDescricao("ONG dedicada ao resgate e adoção de animais abandonados.");
        ong.setRede(redeVO);

        erros = new ArrayList<>();
    }

    @Test
    void deveCriarOngComSucesso() {
        doNothing().when(cepService).preencherEndereco(ongDTO.getEndereco());
        doNothing().when(ongValidacao).validarEnderecoPreenchido(ongDTO.getEndereco());
        doNothing().when(ongValidacao).validate(ongDTO);

        when(passwordEncoder.encode("123456")).thenReturn("senhaCriptografada");
        when(ongRepository.save(any(Ong.class))).thenReturn(ong);

        OngDTO resultado = ongService.create(ongDTO);

        assertNotNull(resultado);
        assertEquals("Amigos dos Animais", resultado.getNome());
        assertEquals("contato@amigosanimais.org", resultado.getEmail());
        assertEquals("senhaCriptografada", resultado.getSenha());
        assertEquals("Maria Silva", resultado.getResponsavel());
    }

    @Test
    void deveLancarExcecaoQuandoOngEstiverNuloNaCriacao() {
        doThrow(new IllegalArgumentException("CEP inválido ou não encontrado")).when(ongValidacao).validate(ongDTO);

        assertThrows(IllegalArgumentException.class, () -> {
            ongService.create(ongDTO);
        });

        verify(ongRepository, never()).save(any(Ong.class));
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaEstiverEmUso() {
        erros.add("E-mail já está em uso");

        doThrow(new ValidacaoException(erros)).when(ongValidacao).validate(ongDTO);

        assertThrows(ValidacaoException.class, () -> {
            ongService.create(ongDTO);
        });

        verify(ongRepository, never()).save(any(Ong.class));
    }

    @Test
    void deveLancarExcecaoQuandoCnpjJaEstiverEmUsoNaCriacao() {
        erros.add("CNPJ já está em uso");

        doThrow(new ValidacaoException(erros)).when(ongValidacao).validate(ongDTO);

        assertThrows(ValidacaoException.class, () -> {
            ongService.create(ongDTO);
        });

        verify(ongRepository, never()).save(any(Ong.class));
    }

    @Test
    void deveLancarExcecaoQuandoCellJaEstiverEmUsoNaCriacao() {
        erros.add("Celular já está em uso");

        doThrow(new ValidacaoException(erros)).when(ongValidacao).validate(ongDTO);

        assertThrows(ValidacaoException.class, () -> {
            ongService.create(ongDTO);
        });

        verify(ongRepository, never()).save(any(Ong.class));
    }

    @Test
    void deveLancarExcecaoQuandoNomeUsuarioJaEstiverEmUsoNaCriacao() {
        erros.add("Nome de Usuário já está em uso");

        doThrow(new ValidacaoException(erros)).when(ongValidacao).validate(ongDTO);

        assertThrows(ValidacaoException.class, () -> {
            ongService.create(ongDTO);
        });

        verify(ongRepository, never()).save(any(Ong.class));
    }

    @Test
    void deveConverterEmailParaLowerCaseAntesDeSalvar() {
        ongDTO.setEmail("EmailMAIUSCULO@Teste.COM");

        doNothing().when(ongValidacao).validate(ongDTO);
        when(passwordEncoder.encode(anyString())).thenReturn("senhaCodificada");
        when(ongRepository.save(any(Ong.class))).thenAnswer(invocation -> {
            Ong ongRetornada = invocation.getArgument(0);
            assertEquals("emailmaiusculo@teste.com", ongRetornada.getEmail());
            return ong;
        });

        ongService.create(ongDTO);

        verify(ongRepository).save(any(Ong.class));
    }

    @Test
    void deveAdicionarLinkSelfNoDTORetornado() {
        doNothing().when(ongValidacao).validate(ongDTO);
        when(passwordEncoder.encode(anyString())).thenReturn("senhaCodificada");
        when(ongRepository.save(any(Ong.class))).thenReturn(ong);

        OngDTO resultado = ongService.create(ongDTO);

        assertFalse(resultado.getLinks().isEmpty());
        assertTrue(resultado.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self")));
    }
}
