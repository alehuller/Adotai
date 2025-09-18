package br.com.alevhvm.adotai.administrador.service;

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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.alevhvm.adotai.administrador.dto.AdministradorDTO;
import br.com.alevhvm.adotai.administrador.dto.AdministradorUpdateDTO;
import br.com.alevhvm.adotai.administrador.model.Administrador;
import br.com.alevhvm.adotai.administrador.repository.AdministradorRepository;
import br.com.alevhvm.adotai.administrador.validations.AdministradorValidacao;
import br.com.alevhvm.adotai.auth.dto.LoginDTO;
import br.com.alevhvm.adotai.auth.dto.TokenDTO;
import br.com.alevhvm.adotai.auth.enums.Roles;
import br.com.alevhvm.adotai.auth.model.LoginIdentityView;
import br.com.alevhvm.adotai.auth.service.TokenService;
import br.com.alevhvm.adotai.common.exceptions.ValidacaoException;
import br.com.alevhvm.adotai.common.mapper.DozerMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Validator;

@ExtendWith(MockitoExtension.class)
public class AdministradorServiceTest {

    @Mock
    private AdministradorRepository administradorRepository;

    @Mock
    private AdministradorValidacao administradorValidacao;

    @Mock
    private PagedResourcesAssembler<AdministradorDTO> assembler;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private Validator validator;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AdministradorService administradorService;

    private AdministradorDTO administradorDTO;
    private AdministradorUpdateDTO adminDiferente;
    private Administrador administradorEntity;
    private List<String> erros;

    @BeforeEach
    void setUp() {
        administradorDTO = new AdministradorDTO();
        administradorDTO.setKey(1L);
        administradorDTO.setNome("AdmTeste");
        administradorDTO.setNomeUsuario("AdmTeste");
        administradorDTO.setEmail("emailteste@teste.com");
        administradorDTO.setSenha("senha123");
        administradorDTO.setCell("(11) 94345-9969");
        administradorDTO.setFotoPerfil("teste foto");

        administradorEntity = new Administrador();
        administradorEntity.setIdAdministrador(1L);
        administradorEntity.setNome("AdmTeste");
        administradorEntity.setNomeUsuario("AdmTeste");
        administradorEntity.setEmail("emailteste@teste.com");
        administradorEntity.setSenha("senhaCodificada");
        administradorEntity.setCell("(11) 94345-9969");
        administradorEntity.setFotoPerfil("teste foto");
        administradorEntity.setRole(Roles.ADMIN);

        adminDiferente = new AdministradorUpdateDTO();
        adminDiferente.setNome("AdmNomeDiferente");
        adminDiferente.setFotoPerfil("FotoDiferente");
        adminDiferente.setEmail("admdiferente@email.com");
        adminDiferente.setSenha("senhaDiferente");
        adminDiferente.setCell("(11) 95555-5555");

        erros = new ArrayList<>();
    }

    @Test
    void deveCriarAdministradorComSucesso() {
        //doNothing utilizado pois nao queremos que ele lance excecao ou qualquer outra coisa que pode quebrar o teste
        doNothing().when(administradorValidacao).validate(administradorDTO);

        when(passwordEncoder.encode("senha123")).thenReturn("senhaCodificada");
        
        when(administradorRepository.save(any(Administrador.class))).thenReturn(administradorEntity);

        AdministradorDTO resultado = administradorService.create(administradorDTO);

        assertNotNull(resultado);
        assertEquals("AdmTeste", resultado.getNome());
        assertEquals("emailteste@teste.com", resultado.getEmail());
        assertEquals("senhaCodificada", resultado.getSenha());
        verify(administradorValidacao).validate(administradorDTO);
        verify(passwordEncoder).encode("senha123");
        verify(administradorRepository).save(any(Administrador.class));
    }

    @Test
    void naoDeveSalvarAdministradorQuandoJaExistirAdministradorComEmailInserido() {
        erros.add("E-mail já está em uso");

        doThrow(new ValidacaoException(erros)).when(administradorValidacao).validate(administradorDTO);

        assertThrows(ValidacaoException.class, () -> {
            administradorService.create(administradorDTO);
        });

        verify(administradorRepository, never()).save(any(Administrador.class));
    }

    @Test
    void naoDeveSalvarAdministradorQuandoJaExistirAdministradorComNomeUsuarioInserido() {
        erros.add("Nome de Usuário já está em uso");

        doThrow(new ValidacaoException(erros)).when(administradorValidacao).validate(administradorDTO);

        assertThrows(ValidacaoException.class, () -> {
            administradorService.create(administradorDTO);
        });

        verify(administradorRepository, never()).save(any(Administrador.class));
    }

    @Test
    void naoDeveSalvarAdministradorQuandoJaExistirAdministradorComCellInserido() {
        erros.add("Cell já está em uso");

        doThrow(new ValidacaoException(erros)).when(administradorValidacao).validate(administradorDTO);

        assertThrows(ValidacaoException.class, () -> {
            administradorService.create(administradorDTO);
        });

        verify(administradorRepository, never()).save(any(Administrador.class));
    }

    @Test
    void naoDeveSalvarAdministradorQuandoAdministradorInseridoForNulo() {
        doThrow(new NullPointerException("Não há dados")).when(administradorValidacao).validate(administradorDTO);

        assertThrows(NullPointerException.class, () -> {
            administradorService.create(administradorDTO);
        });

        verify(administradorRepository, never()).save(any(Administrador.class));
    }

    @Test
    void deveConverterEmailParaLowerCaseAntesDeSalvar() {
        administradorDTO.setEmail("EmailMAIUSCULO@Teste.COM");

        doNothing().when(administradorValidacao).validate(administradorDTO);
        when(passwordEncoder.encode(anyString())).thenReturn("senhaCodificada");
        when(administradorRepository.save(any(Administrador.class))).thenAnswer(invocation -> {
            Administrador adm = invocation.getArgument(0); //invocation.getArgument(0) pega o objeto Administrador que foi passado para salvar
            assertEquals("emailmaiusculo@teste.com", adm.getEmail());
            return administradorEntity;
        });

        administradorService.create(administradorDTO);

        verify(administradorRepository).save(any(Administrador.class));
    }

    @Test
    void deveAdicionarLinkSelfNoDTORetornado() {
        doNothing().when(administradorValidacao).validate(administradorDTO);
        when(passwordEncoder.encode(anyString())).thenReturn("senhaCodificada");
        when(administradorRepository.save(any(Administrador.class))).thenReturn(administradorEntity);

        AdministradorDTO resultado = administradorService.create(administradorDTO);

        assertFalse(resultado.getLinks().isEmpty());
        assertTrue(resultado.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self")));
    }

    @Test
    void deveRetornarPaginaDeAdministradoresComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);

        List<Administrador> admins = List.of(administradorEntity);
        Page<Administrador> adminPage = new PageImpl<>(admins, pageable, admins.size());

        when(administradorRepository.findAll(pageable)).thenReturn(adminPage);

        AdministradorDTO dto = DozerMapper.parseObject(administradorEntity, AdministradorDTO.class);
        List<AdministradorDTO> dtoList = List.of(dto);
        Page<AdministradorDTO> dtoPage = new PageImpl<>(dtoList, pageable, dtoList.size());

        PageMetadata metadata = new PageMetadata(10, 0, 1);
        PagedModel<EntityModel<AdministradorDTO>> pagedModelMock = PagedModel.empty(metadata);
        when(assembler.toModel(any(Page.class), any(Link.class))).thenReturn(pagedModelMock);

        PagedModel<EntityModel<AdministradorDTO>> resultado = administradorService.findAll(pageable);

        assertNotNull(resultado);
        verify(administradorRepository).findAll(pageable);
        verify(assembler).toModel(any(Page.class), any(Link.class));
    }

    @Test
    void deveRetornarPaginaVaziaQuandoNaoExistirAdministradores() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Administrador> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(administradorRepository.findAll(pageable)).thenReturn(emptyPage);

        PageMetadata metadata = new PageMetadata(10, 0, 0);
        PagedModel<EntityModel<AdministradorDTO>> pagedModelMock = PagedModel.empty(metadata);
        when(assembler.toModel(any(Page.class), any(Link.class))).thenReturn(pagedModelMock);

        PagedModel<EntityModel<AdministradorDTO>> resultado = administradorService.findAll(pageable);

        assertNotNull(resultado);
        assertTrue(resultado.getContent().isEmpty());
    }

    @Test
    void deveRetornarAdministradorPeloNomeUsuarioProcuradoComSucesso() {
        when(administradorRepository.findByNomeUsuario("AdmTeste")).thenReturn(Optional.of(administradorEntity));

        //O método converte a entitidade para dto, isto é apenas visual
        /*AdministradorDTO dtoEsperado = DozerMapper.parseObject(administradorEntity, AdministradorDTO.class);
        dtoEsperado.add(Link.of("http://localhost/api/v1/administradores/nomeUsuario/" + administradorEntity.getNomeUsuario()).withSelfRel()); */

        AdministradorDTO resultado = administradorService.findByNomeUsuario("AdmTeste");

        assertNotNull(resultado);
        assertEquals("emailteste@teste.com", resultado.getEmail());
        assertTrue(resultado.getLinks().hasLink("self"));
        assertTrue(resultado.getRequiredLink("self").getHref().endsWith("/api/v1/administradores/nomeUsuario/" + administradorEntity.getNomeUsuario()));

        verify(administradorRepository, times(1)).findByNomeUsuario("AdmTeste");
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarAdministradorPorNomeUsuarioProcurado() {
        when(administradorRepository.findByNomeUsuario("AdmTesteErrado")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            administradorService.findByNomeUsuario("AdmTesteErrado");
        });

        assertEquals("Usuário não encontrado.", ex.getMessage());
    }

    @Test
    void deveRetornarAdministradorPeloIdProcuradoComSucesso() {
        when(administradorRepository.findById(1L)).thenReturn(Optional.of(administradorEntity));

        //O método converte a entitidade para dto, isto é apenas visual
        /*AdministradorDTO dtoEsperado = DozerMapper.parseObject(administradorEntity, AdministradorDTO.class);
        dtoEsperado.add(Link.of("http://localhost/api/v1/administradores/id/" + administradorEntity.getIdAdministrador()).withSelfRel()); */

        AdministradorDTO resultado = administradorService.findById(1L);

        assertNotNull(resultado);
        assertEquals("emailteste@teste.com", resultado.getEmail());
        assertTrue(resultado.getLinks().hasLink("self"));
        assertTrue(resultado.getRequiredLink("self").getHref().endsWith("/api/v1/administradores/" + administradorEntity.getIdAdministrador()));

        verify(administradorRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarAdministradorPorIdProcurado() {
        when(administradorRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            administradorService.findById(2L);
        });

        assertEquals("Administrador não encontrado.", ex.getMessage());
    }

    @Test
    void deveAtualizarAdministradorInteiroComSucesso() {
        when(administradorRepository.findByNomeUsuario("AdmTeste")).thenReturn(Optional.of(administradorEntity));
        when(passwordEncoder.encode(adminDiferente.getSenha())).thenReturn("senhaCriptograda");
        when(administradorRepository.save(any(Administrador.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AdministradorDTO resultado = administradorService.update(adminDiferente, "AdmTeste");

        assertNotNull(resultado);
        assertEquals("AdmNomeDiferente", resultado.getNomeUsuario());
        assertTrue(resultado.getLinks().hasLink("self"));

        verify(administradorRepository).findByNomeUsuario("AdmTeste");
        verify(administradorValidacao).validateUpdate(any(Administrador.class));
        verify(administradorRepository).save(any(Administrador.class));
        verify(passwordEncoder).encode(adminDiferente.getSenha());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarAdministradorPorNomeUsuarioNoUpdate() {
        when(administradorRepository.findByNomeUsuario("AdmTesteErrado")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            administradorService.update(adminDiferente, "AdmTesteErrado");
        });

        assertEquals("Administrador não encontrado.", ex.getMessage());
    }

    @Test
    void deveAtualizarAdministradorPartialComSucesso() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("nomeUsuario", "NovoNome");
        updates.put("email", "novoemail@teste.com");

        when(administradorRepository.findByNomeUsuario("AdmTeste")).thenReturn(Optional.of(administradorEntity));

        doNothing().when(administradorValidacao).validatePartialUpdate("AdmTeste", updates);
        when(administradorRepository.save(any(Administrador.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(validator.validate(any(AdministradorDTO.class))).thenReturn(Collections.emptySet());

        AdministradorDTO resultado = administradorService.partialUpdate("AdmTeste", updates);

        assertNotNull(resultado);
        assertEquals("NovoNome", resultado.getNomeUsuario());
        assertEquals("novoemail@teste.com", resultado.getEmail());
        assertTrue(resultado.getLinks().hasLink("self"));
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarAdministradorNoPartialUpdate() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("nomeUsuario", "NomeUsuarioNovo");

        when(administradorRepository.findByNomeUsuario("AdmTesteErrado")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            administradorService.partialUpdate("AdmTesteErrado", updates);
        });

        assertEquals("Usuário não encontrado.", ex.getMessage());
    }

    @Test
    void deveLogarAdministradorComSucesso() {
        LoginDTO loginDTO = new LoginDTO("emailteste@teste.com", "senha123");

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginDTO.identifier(), loginDTO.password());

        Authentication auth = mock(Authentication.class);
        LoginIdentityView principal = mock(LoginIdentityView.class);
        when(auth.getPrincipal()).thenReturn(principal);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(auth);

        when(tokenService.generateToken(principal)).thenReturn("token123");

        TokenDTO resultado = administradorService.logar(loginDTO);

        assertNotNull(resultado);
        assertEquals("token123", resultado.accessToken());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(tokenService).generateToken(principal);
    }

    @Test
    void deveLancarExcecaoQuandoCredenciasInvalidas() {
        LoginDTO loginDTO = new LoginDTO("emailteste@teste.com", "senhaErrada");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new BadCredentialsException("Credenciais inválidas"));

        assertThrows(BadCredentialsException.class, () -> {
            administradorService.logar(loginDTO);
        });

        verify(authenticationManager).authenticate((any(UsernamePasswordAuthenticationToken.class)));
        verify(tokenService, never()).generateToken(any());
    }

    @Test
    void deveDeletarAdministradorComSucesso() {
        when(administradorRepository.findByNomeUsuario("AdmTeste")).thenReturn(Optional.of(administradorEntity));

        administradorService.delete("AdmTeste");

        verify(administradorRepository).findByNomeUsuario("AdmTeste");
        verify(administradorRepository).delete(administradorEntity);
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarAdministradorNoDelete() {
        when(administradorRepository.findByNomeUsuario("AdmTesteErrado")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            administradorService.delete("AdmTesteErrado");
        });

        assertEquals("Administrador não encontrado", ex.getMessage());
    }
}
