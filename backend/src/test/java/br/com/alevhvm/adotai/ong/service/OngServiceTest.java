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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.alevhvm.adotai.adocao.dto.AdocaoDTO;
import br.com.alevhvm.adotai.adocao.enums.StatusAdocao;
import br.com.alevhvm.adotai.adocao.model.Adocao;
import br.com.alevhvm.adotai.adocao.repository.AdocaoRepository;
import br.com.alevhvm.adotai.animal.dto.AnimalDTO;
import br.com.alevhvm.adotai.animal.enums.StatusAnimal;
import br.com.alevhvm.adotai.animal.model.Animal;
import br.com.alevhvm.adotai.auth.dto.LoginDTO;
import br.com.alevhvm.adotai.auth.dto.TokenDTO;
import br.com.alevhvm.adotai.auth.enums.Roles;
import br.com.alevhvm.adotai.auth.model.LoginIdentityView;
import br.com.alevhvm.adotai.auth.service.TokenService;
import br.com.alevhvm.adotai.common.exceptions.ValidacaoException;
import br.com.alevhvm.adotai.common.mapper.DozerMapper;
import br.com.alevhvm.adotai.common.vo.CnpjVO;
import br.com.alevhvm.adotai.common.vo.DescricaoVO;
import br.com.alevhvm.adotai.common.vo.EnderecoVO;
import br.com.alevhvm.adotai.common.vo.RedeVO;
import br.com.alevhvm.adotai.ong.dto.OngDTO;
import br.com.alevhvm.adotai.ong.dto.OngFiltroDTO;
import br.com.alevhvm.adotai.ong.dto.OngUpdateDTO;
import br.com.alevhvm.adotai.ong.model.Ong;
import br.com.alevhvm.adotai.ong.repository.OngRepository;
import br.com.alevhvm.adotai.ong.validations.OngValidacao;
import br.com.alevhvm.adotai.usuario.model.Usuario;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Validator;

@ExtendWith(MockitoExtension.class)
public class OngServiceTest {

    @InjectMocks
    private OngService ongService;

    @Mock
    private CepService cepService;

    @Mock
    private OngRepository ongRepository;

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private OngValidacao ongValidacao;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private PagedResourcesAssembler<AnimalDTO> assembler;

    @Mock
    private Validator validator;

    private Animal animalEntity;
    private Adocao adocaoEntity;
    private Usuario usuario;
    private Ong ong;
    private OngDTO ongDTO;
    private OngUpdateDTO ongDiferente;

    private DescricaoVO descricaoVO;
    private RedeVO redeVO;
    private List<String> erros;
    private Set<Animal> animaisFavoritos;
    private OngFiltroDTO filtro;

    @BeforeEach
    void setUp() {
        filtro = new OngFiltroDTO();

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
        ongDTO.setRede(redeVO);

        ongDiferente = new OngUpdateDTO();
        ongDiferente.setKey(2L);
        ongDiferente.setNome("Amigos dos Animais Diferente");
        ongDiferente.setNomeUsuario("amigosanimaisdiferente");
        ongDiferente.setFotoPerfil("foto_ong_diferente.png");
        ongDiferente.setEmail("contatodiferente@amigosanimais.org");
        ongDiferente.setSenha("123456");
        ongDiferente.setEndereco(new EnderecoVO(
                "Rua das Flores Diferente",
                "120",
                "Casa 3",
                "SubCentro",
                "Rio de Janeiro",
                "RJ",
                "11111-000"
        ));
        ongDiferente.setCell("11911112222");
        ongDiferente.setResponsavel("Maria Silva Diferente");
        ongDiferente.setDescricao("ONG dedicada ao resgate e adoção de animais abandonados Diferente.");
        ongDiferente.setRede(redeVO);

        erros = new ArrayList<>();

        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setNome("Usuario Nome");
        usuario.setEmail("usuario@email.com");
        usuario.setNomeUsuario("UsuarioNomeUsuario");
        usuario.setFotoPerfil("Foto Usuario");
        usuario.setSenha("senhaCriptografada");
        usuario.setCell("(11) 94345-9969");
        usuario.setCpf("11122233344");
        usuario.setRole(Roles.USER);
        usuario.setAnimaisFavoritos(animaisFavoritos);

        animalEntity = new Animal();
        animalEntity.setIdAnimal(1L);
        animalEntity.setNome("Animal Nome");
        animalEntity.setEspecie("Cachorro");
        animalEntity.setRaca("Spitz Alemão");
        animalEntity.setDataNascimento(LocalDate.parse("2024-07-11"));
        animalEntity.setFoto("Foto Teste");
        animalEntity.setDescricao(descricaoVO);
        animalEntity.setPorte("Porte Teste");
        animalEntity.setSexo("Macho");
        animalEntity.setStatus(StatusAnimal.ADOTADO);
        animalEntity.setOng(ong);

        adocaoEntity = new Adocao();
        adocaoEntity.setIdAdocao(1L);
        adocaoEntity.setDataAdocao(LocalDate.parse("2024-07-11"));
        adocaoEntity.setStatus(StatusAdocao.APROVADA);
        adocaoEntity.setAnimal(animalEntity);
        adocaoEntity.setUsuario(usuario);

        animaisFavoritos = new HashSet<>();
        animaisFavoritos.add(animalEntity);
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

    @Test
    void deveRetornarPaginaDeOngComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);

        List<Ong> ongs = List.of(ong);
        Page<Ong> ongPage = new PageImpl<>(ongs, pageable, ongs.size());

        when(ongRepository.findAll(pageable)).thenReturn(ongPage);

        OngDTO dto = DozerMapper.parseObject(ong, OngDTO.class);
        List<OngDTO> dtoList = List.of(dto);
        Page<OngDTO> dtoPage = new PageImpl<>(dtoList, pageable, dtoList.size());

        PageMetadata metadata = new PageMetadata(10, 0, 1);
        PagedModel<EntityModel<OngDTO>> pagedModelMock = PagedModel.empty(metadata);
        when(assembler.toModel(any(Page.class), any(Link.class))).thenReturn(pagedModelMock);

        PagedModel<EntityModel<OngDTO>> resultado = ongService.findAll(pageable);

        assertNotNull(resultado);
        verify(ongRepository).findAll(pageable);
        verify(assembler).toModel(any(Page.class), any(Link.class));
    }

    @Test
    void deveRetornarPaginaVaziaQuandoNaoExistirOngs() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ong> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(ongRepository.findAll(pageable)).thenReturn(emptyPage);

        PageMetadata metadata = new PageMetadata(10, 0, 0);
        PagedModel<EntityModel<OngDTO>> pagedModelMock = PagedModel.empty(metadata);
        when(assembler.toModel(any(Page.class), any(Link.class))).thenReturn(pagedModelMock);

        PagedModel<EntityModel<OngDTO>> resultado = ongService.findAll(pageable);

        assertNotNull(resultado);
        assertTrue(resultado.getContent().isEmpty());
    }

    @Test
    void deveRetornarOngPeloIdProcuradoComSucesso() {
        when(ongRepository.findById(1L)).thenReturn(Optional.of(ong));

        OngDTO resultado = ongService.findById(1L);

        assertNotNull(resultado);
        assertEquals("contato@amigosanimais.org", resultado.getEmail());
        assertTrue(resultado.getLinks().hasLink("self"));
        assertTrue(resultado.getRequiredLink("self").getHref().endsWith("/api/v1/ongs/" + ong.getIdOng()));

        verify(ongRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarOngPorIdProcurado() {
        when(ongRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            ongService.findById(2L);
        });

        assertEquals("Ong não encontrada.", ex.getMessage());
    }

    @Test
    void deveRetornarOngPeloNomeUsuarioProcuradoComSucesso() {
        when(ongRepository.findByNomeUsuario("amigosanimais")).thenReturn(Optional.of(ong));

        OngDTO resultado = ongService.findByNomeUsuario("amigosanimais");

        assertNotNull(resultado);
        assertEquals("contato@amigosanimais.org", resultado.getEmail());
        assertTrue(resultado.getLinks().hasLink("self"));
        assertTrue(resultado.getRequiredLink("self").getHref().endsWith("/api/v1/ongs/nomeUsuario/" + ong.getNomeUsuario()));

        verify(ongRepository, times(1)).findByNomeUsuario("amigosanimais");
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarOngPorNomeUsuarioProcurado() {
        when(ongRepository.findByNomeUsuario("amigosanimaiserrado")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            ongService.findByNomeUsuario("amigosanimaiserrado");
        });

        assertEquals("Ong não encontrada.", ex.getMessage());
    }

    @Test
    void deveRetornarPaginaDeAdocoesPorIdDaOngComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);

        List<Adocao> adocoes = List.of(adocaoEntity);
        Page<Adocao> adocaoPage = new PageImpl<>(adocoes, pageable, adocoes.size());

        when(adocaoRepository.findAdocoesByOngId(1L, pageable)).thenReturn(adocaoPage);

        AdocaoDTO dto = DozerMapper.parseObject(adocaoEntity, AdocaoDTO.class);
        List<AdocaoDTO> dtoList = List.of(dto);
        Page<AdocaoDTO> dtoPage = new PageImpl<>(dtoList, pageable, dtoList.size());

        PageMetadata metadata = new PageMetadata(10, 0, 1);
        PagedModel<EntityModel<AdocaoDTO>> pagedModelMock = PagedModel.empty(metadata);
        when(assembler.toModel(any(Page.class), any(Link.class))).thenReturn(pagedModelMock);

        PagedModel<EntityModel<AdocaoDTO>> resultado = ongService.findAllAdocoesByOngId(1L, pageable);

        assertNotNull(resultado);
        verify(adocaoRepository).findAdocoesByOngId(1L, pageable);
        verify(assembler).toModel(any(Page.class), any(Link.class));
    }

    @Test
    void deveRetornarPaginaDeAdocoesPorIdDaOngVazia() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Adocao> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(adocaoRepository.findAdocoesByOngId(1L, pageable)).thenReturn(emptyPage);

        PageMetadata metadata = new PageMetadata(10, 0, 0);
        PagedModel<EntityModel<AdocaoDTO>> pagedModelMock = PagedModel.empty(metadata);
        when(assembler.toModel(any(Page.class), any(Link.class))).thenReturn(pagedModelMock);

        PagedModel<EntityModel<AdocaoDTO>> resultado = ongService.findAllAdocoesByOngId(1L, pageable);

        assertNotNull(resultado);
        assertTrue(resultado.getContent().isEmpty());
    }

    @Test
    void deveRetornarOsOngsPeloFiltro() {
        filtro.setNome("Amigos dos Animais");
        Pageable pageable = PageRequest.of(0, 10);

        List<Ong> ongs = List.of(ong);
        Page<Ong> ongPage = new PageImpl<>(ongs, pageable, ongs.size());

        when(ongRepository.filtrarOngsNativo(filtro, pageable)).thenReturn(ongPage);

        Page<OngDTO> resultado = ongService.filtrarOngs(filtro, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("Amigos dos Animais", resultado.getContent().get(0).getNome());

        verify(ongRepository).filtrarOngsNativo(filtro, pageable);
    }

    @Test
    void deveRetornarPaginaVazioDeAnimaisPeloFiltro() {
        filtro.setNome("Ong Nome Inexistente");
        Pageable pageable = PageRequest.of(0, 10);
        Page<Ong> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(ongRepository.filtrarOngsNativo(filtro, pageable)).thenReturn(emptyPage);

        Page<OngDTO> resultado = ongService.filtrarOngs(filtro, pageable);

        assertNotNull(resultado);
        assertTrue(resultado.getContent().isEmpty());
    }

    @Test
    void deveLogarOngComSucesso() {
        LoginDTO loginDTO = new LoginDTO("contato@amigosanimais.org", "123456");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginDTO.identifier(), loginDTO.password());

        Authentication auth = mock(Authentication.class);
        LoginIdentityView principal = mock(LoginIdentityView.class);
        when(auth.getPrincipal()).thenReturn(principal).thenReturn("token123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

        when(tokenService.generateToken(principal)).thenReturn("token123");

        TokenDTO resultado = ongService.logar(loginDTO);

        assertNotNull(resultado);
        assertEquals("token123", resultado.accessToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).generateToken(principal);
    }

    @Test
    void deveLancarExcecaoQuandoCredenciasInvalidas() {
        LoginDTO loginDTO = new LoginDTO("contatoerrado@amigosanimais.org", "senhaErrada");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new BadCredentialsException("Credenciais inválidas"));

        assertThrows(BadCredentialsException.class, () -> {
            ongService.logar(loginDTO);
        });

        verify(authenticationManager).authenticate((any(UsernamePasswordAuthenticationToken.class)));
        verify(tokenService, never()).generateToken(any());
    }

    @Test
    void deveAtualizarOngInteiraComSucesso() {
        when(ongRepository.findByNomeUsuario("amigosanimais")).thenReturn(Optional.of(ong));
        when(passwordEncoder.encode(ongDiferente.getSenha())).thenReturn("senhaCriptograda");
        when(ongRepository.save(any(Ong.class))).thenAnswer(invocation -> invocation.getArgument(0));

        OngDTO resultado = ongService.update(ongDiferente, "amigosanimais");

        assertNotNull(resultado);
        assertEquals("amigosanimaisdiferente", resultado.getNomeUsuario());
        assertEquals("contatodiferente@amigosanimais.org", resultado.getEmail());
        assertEquals("Maria Silva Diferente", resultado.getResponsavel());
        assertTrue(resultado.getLinks().hasLink("self"));

        verify(ongRepository).findByNomeUsuario("amigosanimais");
        verify(cepService).preencherEndereco(ongDiferente.getEndereco());
        verify(ongValidacao).validarEnderecoPreenchido(ongDiferente.getEndereco());
        verify(ongValidacao).validateUpdate(any(Ong.class));
        verify(ongRepository).save(any(Ong.class));
        verify(passwordEncoder).encode(ongDiferente.getSenha());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarOngPorNomeUsuarioNoUpdate() {
        when(ongRepository.findByNomeUsuario("OngNomeUsuarioErrado")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            ongService.update(ongDiferente, "OngNomeUsuarioErrado");
        });

        assertEquals("Ong não encontrada.", ex.getMessage());
    }

    @Test
    void deveAtualizarOngPartialComSucesso() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("nomeUsuario", "NovoNome");
        updates.put("email", "novoemail@teste.com");

        when(ongRepository.findByNomeUsuario("amigosanimais")).thenReturn(Optional.of(ong));

        doNothing().when(ongValidacao).validatePartialUpdate("amigosanimais", updates);
        when(ongRepository.save(any(Ong.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(validator.validate(any(OngDTO.class))).thenReturn(Collections.emptySet());

        OngDTO resultado = ongService.partialUpdate("amigosanimais", updates);

        assertNotNull(resultado);
        assertEquals("NovoNome", resultado.getNomeUsuario());
        assertEquals("novoemail@teste.com", resultado.getEmail());
        assertTrue(resultado.getLinks().hasLink("self"));
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarOngNoPartialUpdate() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("nomeUsuario", "NomeUsuarioNovo");

        when(ongRepository.findByNomeUsuario("UsuarioNomeUsuarioErrado")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            ongService.partialUpdate("UsuarioNomeUsuarioErrado", updates);
        });

        assertEquals("Ong não encontrada.", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioTentarAtualizarCnpjDaOngNoPartialUpdate() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("cnpj", "53.962.605/0001-20");

        erros.add("Não é permitido alterar o CNPJ.");

        when(ongRepository.findByNomeUsuario("amigosanimais")).thenReturn(Optional.of(ong));
        doThrow(new ValidacaoException(erros)).when(ongValidacao).validatePartialUpdate("amigosanimais", updates);

        assertThrows(ValidacaoException.class, () -> {
            ongService.partialUpdate("amigosanimais", updates);
        });
    }

    @Test
    void deveDeletarOngComSucesso() {
        when(ongRepository.findByNomeUsuario("amigosanimais")).thenReturn(Optional.of(ong));

        ongService.delete("amigosanimais");

        verify(ongRepository).findByNomeUsuario("amigosanimais");
        verify(ongRepository).deleteByNomeUsuario("amigosanimais");
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarOngNoDelete() {
        when(ongRepository.findByNomeUsuario("OngNomeUsuarioErrado")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            ongService.delete("OngNomeUsuarioErrado");
        });

        assertEquals("Ong não encontrada.", ex.getMessage());
    }
}
