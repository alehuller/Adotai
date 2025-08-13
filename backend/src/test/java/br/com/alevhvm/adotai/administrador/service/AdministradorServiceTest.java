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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.alevhvm.adotai.administrador.dto.AdministradorDTO;
import br.com.alevhvm.adotai.administrador.model.Administrador;
import br.com.alevhvm.adotai.administrador.repository.AdministradorRepository;
import br.com.alevhvm.adotai.administrador.validations.AdministradorValidacao;
import br.com.alevhvm.adotai.auth.enums.Roles;
import br.com.alevhvm.adotai.common.exceptions.ValidacaoException;
import br.com.alevhvm.adotai.common.mapper.DozerMapper;

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

    @InjectMocks
    private AdministradorService administradorService;

    private AdministradorDTO administradorDTO;
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
}
