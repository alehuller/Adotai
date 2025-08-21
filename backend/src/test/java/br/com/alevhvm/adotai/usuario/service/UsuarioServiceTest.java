package br.com.alevhvm.adotai.usuario.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
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
import org.mockito.ArgumentCaptor;
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
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.alevhvm.adotai.adocao.dto.AdocaoDTO;
import br.com.alevhvm.adotai.adocao.enums.StatusAdocao;
import br.com.alevhvm.adotai.adocao.model.Adocao;
import br.com.alevhvm.adotai.adocao.repository.AdocaoRepository;
import br.com.alevhvm.adotai.animal.dto.AnimalDTO;
import br.com.alevhvm.adotai.animal.enums.StatusAnimal;
import br.com.alevhvm.adotai.animal.model.Animal;
import br.com.alevhvm.adotai.animal.repository.AnimalRepository;
import br.com.alevhvm.adotai.auth.dto.LoginDTO;
import br.com.alevhvm.adotai.auth.dto.RegistroDTO;
import br.com.alevhvm.adotai.auth.dto.TokenDTO;
import br.com.alevhvm.adotai.auth.enums.Roles;
import br.com.alevhvm.adotai.auth.model.LoginIdentityView;
import br.com.alevhvm.adotai.auth.service.TokenService;
import br.com.alevhvm.adotai.common.exceptions.ValidacaoException;
import br.com.alevhvm.adotai.common.mapper.DozerMapper;
import br.com.alevhvm.adotai.common.vo.CpfVO;
import br.com.alevhvm.adotai.common.vo.DescricaoVO;
import br.com.alevhvm.adotai.common.vo.EnderecoVO;
import br.com.alevhvm.adotai.common.vo.RedeVO;
import br.com.alevhvm.adotai.ong.model.Ong;
import br.com.alevhvm.adotai.usuario.dto.UsuarioDTO;
import br.com.alevhvm.adotai.usuario.dto.UsuarioUpdateDTO;
import br.com.alevhvm.adotai.usuario.model.Usuario;
import br.com.alevhvm.adotai.usuario.repository.UsuarioRepository;
import br.com.alevhvm.adotai.usuario.validations.UsuarioValidacao;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Validator;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private UsuarioValidacao usuarioValidacao;

    @Mock
    private PagedResourcesAssembler<AnimalDTO> assembler;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Validator validator;

    private RegistroDTO registroDTO;
    private UsuarioDTO usuarioDTO;
    private UsuarioUpdateDTO usuarioDiferente;
    private Usuario usuario;
    private Animal animalEntity;
    private Adocao adocaoEntity;
    private Ong ong;

    private DescricaoVO descricaoVO;
    private RedeVO redeVO;
    private CpfVO cpf;
    private Set<Animal> animaisFavoritos;
    private List<String> erros;

    @BeforeEach
    void setUp() {
        redeVO = new RedeVO();
        redeVO.setInstagram("https://teste.adotai.com/");
        redeVO.setFacebook("https://teste.facebook.com/adotai");

        descricaoVO = new DescricaoVO();
        descricaoVO.setGeral("Descrição Teste");
        descricaoVO.setHistoricoSaude("Historico Teste");
        descricaoVO.setVacinacao("Vacinacao Teste");

        ong = new Ong();
        ong.setIdOng(1L);
        ong.setNome("Amigos dos Animais");
        ong.setNomeUsuario("amigosanimais");
        ong.setEmail("contato@amigosanimais.org");
        ong.setSenha("123456");
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

        animaisFavoritos = new HashSet<>();
        animaisFavoritos.add(animalEntity);

        cpf = new CpfVO("111222233344");
        cpf.setCpf("111222233344");

        registroDTO = new RegistroDTO();
        registroDTO.setEmail("usuario@email.com");
        registroDTO.setPassword("senha123");
        registroDTO.setNome("Usuario Nome");
        registroDTO.setNomeUsuario("UsuarioNomeUsuario");
        registroDTO.setFotoPerfil("Foto Usuario");
        registroDTO.setCell("(11) 94345-9969");
        registroDTO.setCpf(cpf);

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setKey(1L);
        usuarioDTO.setNome("Usuario Nome");
        usuarioDTO.setEmail("usuario@email.com");
        usuarioDTO.setNomeUsuario("UsuarioNomeUsuario");
        usuarioDTO.setFotoPerfil("Foto Usuario");
        usuarioDTO.setSenha("senhaCriptografada");
        usuarioDTO.setCell("(11) 94345-9969");
        usuarioDTO.setCpf(cpf);

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

        erros = new ArrayList<>();

        adocaoEntity = new Adocao();
        adocaoEntity.setIdAdocao(1L);
        adocaoEntity.setDataAdocao(LocalDate.parse("2024-07-11"));
        adocaoEntity.setStatus(StatusAdocao.APROVADA);
        adocaoEntity.setAnimal(animalEntity);
        adocaoEntity.setUsuario(usuario);

        usuarioDiferente = new UsuarioUpdateDTO();
        usuarioDiferente.setNome("Usuario Nome Diferente");
        usuarioDiferente.setEmail("usuariodiferente@email.com");
        usuarioDiferente.setNomeUsuario("UsuarioNomeUsuarioDiferente");
        usuarioDiferente.setFotoPerfil("Foto Usuario Diferente");
        usuarioDiferente.setSenha("senhaCriptografada");
        usuarioDiferente.setCell("(11) 94345-9970");
    }

    @Test
    void deveCriarUsuarioComSucesso() {
        doNothing().when(usuarioValidacao).validate(registroDTO);

        when(passwordEncoder.encode("senha123")).thenReturn("senhaCriptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioDTO resultado = usuarioService.create(registroDTO);

        assertNotNull(resultado);
        assertEquals("Usuario Nome", resultado.getNome());
        assertEquals("usuario@email.com", resultado.getEmail());
        assertEquals("UsuarioNomeUsuario", resultado.getNomeUsuario());
        assertEquals("senhaCriptografada", resultado.getSenha());

        verify(usuarioValidacao).validate(registroDTO);
        verify(passwordEncoder).encode("senha123");
        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioEstiverNuloNaCriacao() {
        doThrow(new NullPointerException("Não há dados")).when(usuarioValidacao).validate(registroDTO);

        assertThrows(NullPointerException.class, () -> {
            usuarioService.create(registroDTO);
        });

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void deveLancarExcecaoQuandoEmailJaEstiverEmUsoNaCriacao() {
        erros.add("E-mail já está em uso");

        doThrow(new ValidacaoException(erros)).when(usuarioValidacao).validate(registroDTO);

        assertThrows(ValidacaoException.class, () -> {
            usuarioService.create(registroDTO);
        });

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void deveLancarExcecaoQuandoCpfJaEstiverEmUsoNaCriacao() {
        erros.add("CPF já está em uso");

        doThrow(new ValidacaoException(erros)).when(usuarioValidacao).validate(registroDTO);

        assertThrows(ValidacaoException.class, () -> {
            usuarioService.create(registroDTO);
        });

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void deveLancarExcecaoQuandoCellJaEstiverEmUsoNaCriacao() {
        erros.add("Cell já está em uso");

        doThrow(new ValidacaoException(erros)).when(usuarioValidacao).validate(registroDTO);

        assertThrows(ValidacaoException.class, () -> {
            usuarioService.create(registroDTO);
        });

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void deveLancarExcecaoQuandoNomeUsuarioJaEstiverEmUsoNaCriacao() {
        erros.add("Nome de Usuário já está em uso");

        doThrow(new ValidacaoException(erros)).when(usuarioValidacao).validate(registroDTO);

        assertThrows(ValidacaoException.class, () -> {
            usuarioService.create(registroDTO);
        });

        verify(usuarioRepository, never()).save(any(Usuario.class));
    }

    @Test
    void deveConverterEmailParaLowerCaseAntesDeSalvar() {
        registroDTO.setEmail("EmailMAIUSCULO@Teste.COM");

        doNothing().when(usuarioValidacao).validate(registroDTO);
        when(passwordEncoder.encode(anyString())).thenReturn("senhaCodificada");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> {
            Usuario usuarioRetornado = invocation.getArgument(0);
            assertEquals("emailmaiusculo@teste.com", usuarioRetornado.getEmail());
            return usuario;
        });

        usuarioService.create(registroDTO);

        verify(usuarioRepository).save(any(Usuario.class));
    }

    @Test
    void deveAdicionarLinkSelfNoDTORetornado() {
        doNothing().when(usuarioValidacao).validate(registroDTO);
        when(passwordEncoder.encode(anyString())).thenReturn("senhaCodificada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        UsuarioDTO resultado = usuarioService.create(registroDTO);

        assertFalse(resultado.getLinks().isEmpty());
        assertTrue(resultado.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self")));
    }

    @Test
    void deveRetornarPaginaDeUsuariosComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);

        List<Usuario> usuarios = List.of(usuario);
        Page<Usuario> usuarioPage = new PageImpl<>(usuarios, pageable, usuarios.size());

        when(usuarioRepository.findAll(pageable)).thenReturn(usuarioPage);

        UsuarioDTO dto = DozerMapper.parseObject(usuario, UsuarioDTO.class);
        List<UsuarioDTO> dtoList = List.of(dto);
        Page<UsuarioDTO> dtoPage = new PageImpl<>(dtoList, pageable, dtoList.size());

        PageMetadata metadata = new PageMetadata(10, 0, 1);
        PagedModel<EntityModel<UsuarioDTO>> pagedModelMock = PagedModel.empty(metadata);
        when(assembler.toModel(any(Page.class), any(Link.class))).thenReturn(pagedModelMock);

        PagedModel<EntityModel<UsuarioDTO>> resultado = usuarioService.findAll(pageable);

        assertNotNull(resultado);
        verify(usuarioRepository).findAll(pageable);
        verify(assembler).toModel(any(Page.class), any(Link.class));
    }

    @Test
    void deveRetornarPaginaVaziaQuandoNaoExistirUsuarios() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Usuario> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(usuarioRepository.findAll(pageable)).thenReturn(emptyPage);

        PageMetadata metadata = new PageMetadata(10, 0, 0);
        PagedModel<EntityModel<UsuarioDTO>> pagedModelMock = PagedModel.empty(metadata);
        when(assembler.toModel(any(Page.class), any(Link.class))).thenReturn(pagedModelMock);

        PagedModel<EntityModel<UsuarioDTO>> resultado = usuarioService.findAll(pageable);

        assertNotNull(resultado);
        assertTrue(resultado.getContent().isEmpty());
    }

    @Test
    void deveRetornarUsuarioPeloNomeUsuarioProcuradoComSucesso() {
        when(usuarioRepository.findByNomeUsuario("UsuarioNomeUsuario")).thenReturn(Optional.of(usuario));

        UsuarioDTO resultado = usuarioService.findByNomeUsuario("UsuarioNomeUsuario");

        assertNotNull(resultado);
        assertEquals("usuario@email.com", resultado.getEmail());
        assertTrue(resultado.getLinks().hasLink("self"));
        assertTrue(resultado.getRequiredLink("self").getHref().endsWith("/api/v1/usuarios/nomeUsuario/" + usuario.getNomeUsuario()));

        verify(usuarioRepository, times(1)).findByNomeUsuario("UsuarioNomeUsuario");
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarUsuarioPorNomeUsuarioProcurado() {
        when(usuarioRepository.findByNomeUsuario("UsuarioNomeUsuarioErrado")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.findByNomeUsuario("UsuarioNomeUsuarioErrado");
        });

        assertEquals("Usuário não encontrado.", ex.getMessage());
    }

    @Test
    void deveRetornarUsuarioPeloIdProcuradoComSucesso() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        UsuarioDTO resultado = usuarioService.findById(1L);

        assertNotNull(resultado);
        assertEquals("usuario@email.com", resultado.getEmail());
        assertTrue(resultado.getLinks().hasLink("self"));
        assertTrue(resultado.getRequiredLink("self").getHref().endsWith("/api/v1/usuarios/" + usuario.getIdUsuario()));

        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarUsuarioPorIdProcurado() {
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.findById(2L);
        });

        assertEquals("Usuário não encontrado.", ex.getMessage());
    }

    @Test
    void deveRetornarPaginaDeAdocoesPorNomeUsuarioComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);

        List<Adocao> adocoes = List.of(adocaoEntity);
        Page<Adocao> adocaoPage = new PageImpl<>(adocoes, pageable, adocoes.size());

        when(adocaoRepository.findAdocoesByNomeUsuario("UsuarioNomeUsuario", pageable)).thenReturn(adocaoPage);

        AdocaoDTO dto = DozerMapper.parseObject(adocaoEntity, AdocaoDTO.class);
        List<AdocaoDTO> dtoList = List.of(dto);
        Page<AdocaoDTO> dtoPage = new PageImpl<>(dtoList, pageable, dtoList.size());

        PageMetadata metadata = new PageMetadata(10, 0, 1);
        PagedModel<EntityModel<AdocaoDTO>> pagedModelMock = PagedModel.empty(metadata);
        when(assembler.toModel(any(Page.class), any(Link.class))).thenReturn(pagedModelMock);

        PagedModel<EntityModel<AdocaoDTO>> resultado = usuarioService.findAllAdocoesByNomeUsuario("UsuarioNomeUsuario", pageable);

        assertNotNull(resultado);
        verify(adocaoRepository).findAdocoesByNomeUsuario("UsuarioNomeUsuario", pageable);
        verify(assembler).toModel(any(Page.class), any(Link.class));
    }

    @Test
    void deveRetornarPaginaDeAdocoesPorNomeUsuarioVazia() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Adocao> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(adocaoRepository.findAdocoesByNomeUsuario("UsuarioNomeUsuario", pageable)).thenReturn(emptyPage);

        PageMetadata metadata = new PageMetadata(10, 0, 0);
        PagedModel<EntityModel<AdocaoDTO>> pagedModelMock = PagedModel.empty(metadata);
        when(assembler.toModel(any(Page.class), any(Link.class))).thenReturn(pagedModelMock);

        PagedModel<EntityModel<AdocaoDTO>> resultado = usuarioService.findAllAdocoesByNomeUsuario("UsuarioNomeUsuario", pageable);

        assertNotNull(resultado);
        assertTrue(resultado.getContent().isEmpty());
    }

    @Test
    void deveLogarUsuarioComSucesso() {
        LoginDTO loginDTO = new LoginDTO("usuario@email.com", "senha123");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginDTO.identifier(), loginDTO.password());

        Authentication auth = mock(Authentication.class);
        LoginIdentityView principal = mock(LoginIdentityView.class);
        when(auth.getPrincipal()).thenReturn(principal);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

        when(tokenService.generateToken(principal)).thenReturn("token123");

        TokenDTO resultado = usuarioService.logar(loginDTO);

        assertNotNull(resultado);
        assertEquals("token123", resultado.accessToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).generateToken(principal);
    }

    @Test
    void deveLancarExcecaoQuandoCredenciasInvalidas() {
        LoginDTO loginDTO = new LoginDTO("usuario@email.com", "senhaErrada");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new BadCredentialsException("Credenciais inválidas"));

        assertThrows(BadCredentialsException.class, () -> {
            usuarioService.logar(loginDTO);
        });

        verify(authenticationManager).authenticate((any(UsernamePasswordAuthenticationToken.class)));
        verify(tokenService, never()).generateToken(any());
    }

    @Test
    void deveAtualizarUsuarioInteiroComSucesso() {
        when(usuarioRepository.findByNomeUsuario("UsuarioNomeUsuario")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode(usuarioDiferente.getSenha())).thenReturn("senhaCriptograda");
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UsuarioDTO resultado = usuarioService.update(usuarioDiferente, "UsuarioNomeUsuario");

        assertNotNull(resultado);
        assertEquals("UsuarioNomeUsuarioDiferente", resultado.getNomeUsuario());
        assertTrue(resultado.getLinks().hasLink("self"));

        verify(usuarioRepository).findByNomeUsuario("UsuarioNomeUsuario");
        verify(usuarioValidacao).validateUpdate(any(Usuario.class));
        verify(usuarioRepository).save(any(Usuario.class));
        verify(passwordEncoder).encode(usuarioDiferente.getSenha());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarUsuarioPorNomeUsuarioNoUpdate() {
        when(usuarioRepository.findByNomeUsuario("UsuarioNomeUsuarioErrado")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.update(usuarioDiferente, "UsuarioNomeUsuarioErrado");
        });

        assertEquals("Usuário não encontrado.", ex.getMessage());
    }

    @Test
    void deveAtualizarUsuarioPartialComSucesso() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("nomeUsuario", "NovoNome");
        updates.put("email", "novoemail@teste.com");

        when(usuarioRepository.findByNomeUsuario("UsuarioNomeUsuario")).thenReturn(Optional.of(usuario));

        doNothing().when(usuarioValidacao).validatePartialUpdate("UsuarioNomeUsuario", updates);
        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(validator.validate(any(UsuarioDTO.class))).thenReturn(Collections.emptySet());

        UsuarioDTO resultado = usuarioService.partialUpdate("UsuarioNomeUsuario", updates);

        assertNotNull(resultado);
        assertEquals("NovoNome", resultado.getNomeUsuario());
        assertEquals("novoemail@teste.com", resultado.getEmail());
        assertTrue(resultado.getLinks().hasLink("self"));
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarUsuarioNoPartialUpdate() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("nomeUsuario", "NomeUsuarioNovo");

        when(usuarioRepository.findByNomeUsuario("UsuarioNomeUsuarioErrado")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.partialUpdate("UsuarioNomeUsuarioErrado", updates);
        });

        assertEquals("Usuário não encontrado.", ex.getMessage());
    }

    @Test
    void deveDeletarUsuarioComSucesso() {
        when(usuarioRepository.findByNomeUsuario("UsuarioNomeUsuario")).thenReturn(Optional.of(usuario));

        usuarioService.delete("UsuarioNomeUsuario");

        verify(usuarioRepository).findByNomeUsuario("UsuarioNomeUsuario");
        verify(usuarioRepository).deleteByNomeUsuario("UsuarioNomeUsuario");
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarUsuarioNoDelete() {
        when(usuarioRepository.findByNomeUsuario("UsuarioNomeUsuarioErrado")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.delete("UsuarioNomeUsuarioErrado");
        });

        assertEquals("Usuário não encontrado", ex.getMessage());
    }

    @Test
    void deveRetornarPaginaDeAnimaisFavoritosPorNomeUsuario() {
        Pageable pageable = PageRequest.of(0, 10);

        List<Animal> animais = List.of(animalEntity);
        Page<Animal> animalPage = new PageImpl<>(animais, pageable, animais.size());

        when(usuarioRepository.findAnimaisFavoritosByNomeUsuario("UsuarioNomeUsuario", pageable)).thenReturn(animalPage);

        PageMetadata metadata = new PageMetadata(10, 0 , 1);
        PagedModel<EntityModel<AnimalDTO>> pagedModelMock = PagedModel.empty(metadata);
        when(assembler.toModel(any(Page.class), any(Link.class))).thenReturn(pagedModelMock);

        PagedModel<EntityModel<AnimalDTO>> resultado = usuarioService.findAnimaisFavoritosByNomeUsuario("UsuarioNomeUsuario", pageable);

        assertNotNull(resultado);
        assertSame(pagedModelMock, resultado);
        verify(usuarioRepository).findAnimaisFavoritosByNomeUsuario("UsuarioNomeUsuario", pageable);

        ArgumentCaptor<Page<AnimalDTO>> pageCaptor = ArgumentCaptor.forClass(Page.class);
        verify(assembler).toModel(pageCaptor.capture(), any(Link.class));

        Page<AnimalDTO> pagePassada = pageCaptor.getValue();
        assertEquals(1, pagePassada.getTotalElements());
        assertEquals("Animal Nome", pagePassada.getContent().get(0).getNome());
    }

    @Test
    void deveRetornarPaginaVaziaQuandoNaoExistirAnimaisFavoritosPorNomeUsuario() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Animal> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(usuarioRepository.findAnimaisFavoritosByNomeUsuario("UsuarioNomeUsuarioErrado", pageable)).thenReturn(emptyPage);

        PageMetadata metadata = new PageMetadata(10, 0, 0);
        PagedModel<EntityModel<AnimalDTO>> pagedModelMock = PagedModel.empty(metadata);
        when(assembler.toModel(any(Page.class), any(Link.class))).thenReturn(pagedModelMock);

        PagedModel<EntityModel<AnimalDTO>> resultado = usuarioService.findAnimaisFavoritosByNomeUsuario("UsuarioNomeUsuarioErrado", pageable);

        assertNotNull(resultado);
        assertSame(pagedModelMock, resultado);
        assertTrue(resultado.getContent().isEmpty());
    }

    @Test
    void deveRetornarTrueQuandoUsuarioFavoritouOAnimal() {
        when(usuarioRepository.existsByNomeUsuarioAndAnimaisFavoritos_IdAnimal("UsuarioNomeUsuario", 1L)).thenReturn(true);

        Boolean resultado = usuarioService.isAnimalFavorito("UsuarioNomeUsuario", 1L);

        assertTrue(resultado);
        verify(usuarioRepository, times(1)).existsByNomeUsuarioAndAnimaisFavoritos_IdAnimal("UsuarioNomeUsuario", 1L);
    }

    @Test
    void deveRetornarFalseQuandoUsuarioNaoFavoritouOAnimal() {
        when(usuarioRepository.existsByNomeUsuarioAndAnimaisFavoritos_IdAnimal("UsuarioNomeUsuario", 1L)).thenReturn(false);

        Boolean resultado = usuarioService.isAnimalFavorito("UsuarioNomeUsuario", 1L);

        assertFalse(resultado);
        verify(usuarioRepository, times(1)).existsByNomeUsuarioAndAnimaisFavoritos_IdAnimal("UsuarioNomeUsuario", 1L);
    }

    @Test
    void deveRetornarFalseQuandoOAnimalEstaFavoritado() {
        when(usuarioRepository.existsByNomeUsuarioAndAnimaisFavoritos_IdAnimal("UsuarioNomeUsuario", 1L)).thenReturn(true);

        Boolean resultado = usuarioService.toggleAnimalFavorito("UsuarioNomeUsuario", 1L);

        assertFalse(resultado);
        verify(usuarioRepository, times(1)).existsByNomeUsuarioAndAnimaisFavoritos_IdAnimal("UsuarioNomeUsuario", 1L);
        verify(usuarioRepository, times(1)).removerAnimalDosFavoritos("UsuarioNomeUsuario", 1L);
    }

    @Test
    void deveRetornarTrueQuandoOAnimalNaoEstaFavoritado() {
        when(usuarioRepository.existsByNomeUsuarioAndAnimaisFavoritos_IdAnimal("UsuarioNomeUsuario", 1L)).thenReturn(false);

        Boolean resultado = usuarioService.toggleAnimalFavorito("UsuarioNomeUsuario", 1L);

        assertTrue(resultado);
        verify(usuarioRepository, times(1)).existsByNomeUsuarioAndAnimaisFavoritos_IdAnimal("UsuarioNomeUsuario", 1L);
        verify(usuarioRepository, times(1)).adicionarAnimalAosFavoritos("UsuarioNomeUsuario", 1L);
    }

    @Test
    void deveSalvarAnimalFavoritoParaUsuario() {
        when(usuarioRepository.findByNomeUsuario("UsuarioNomeUsuario")).thenReturn(Optional.of(usuario));
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animalEntity));

        usuarioService.adicionarAnimalFavorito("UsuarioNomeUsuario", 1L);

        assertTrue(usuario.getAnimaisFavoritos().contains(animalEntity));
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarUsuarioNoAdicionarAnimalFavorito() {
        when(usuarioRepository.findByNomeUsuario("UsuarioNomeUsuarioErrado")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.adicionarAnimalFavorito("UsuarioNomeUsuarioErrado", 1L);
        });

        assertEquals("Usuário não encontrado", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarAnimalNoAdicionarAnimalFavorito() {
        when(usuarioRepository.findByNomeUsuario("UsuarioNomeUsuario")).thenReturn(Optional.of(usuario));
        when(animalRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.adicionarAnimalFavorito("UsuarioNomeUsuario", 2L);
        });

        assertEquals("Animal não encontrado", ex.getMessage());
    }

    @Test
    void deveRemoverAnimalFavorito() {
        when(usuarioRepository.findByNomeUsuario("UsuarioNomeUsuario")).thenReturn(Optional.of(usuario));

        usuarioService.removerAnimalFavorito("UsuarioNomeUsuario", 1L);

        assertFalse(usuario.getAnimaisFavoritos().contains(animalEntity));
        verify(usuarioRepository).save(usuario);
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarUsuarioNoRemoverAnimalFavorito() {
        when(usuarioRepository.findByNomeUsuario("UsuarioNomeUsuarioErrado")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            usuarioService.removerAnimalFavorito("UsuarioNomeUsuarioErrado", 1L);
        });

        assertEquals("Usuário não encontrado", ex.getMessage());
        verify(usuarioRepository, never()).save(usuario);
    }
}
