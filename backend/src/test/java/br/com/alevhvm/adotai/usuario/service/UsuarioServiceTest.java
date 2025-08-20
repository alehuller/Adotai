package br.com.alevhvm.adotai.usuario.service;

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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.alevhvm.adotai.administrador.dto.AdministradorDTO;
import br.com.alevhvm.adotai.animal.enums.StatusAnimal;
import br.com.alevhvm.adotai.animal.model.Animal;
import br.com.alevhvm.adotai.auth.dto.RegistroDTO;
import br.com.alevhvm.adotai.auth.enums.Roles;
import br.com.alevhvm.adotai.common.exceptions.ValidacaoException;
import br.com.alevhvm.adotai.common.mapper.DozerMapper;
import br.com.alevhvm.adotai.common.vo.CpfVO;
import br.com.alevhvm.adotai.common.vo.DescricaoVO;
import br.com.alevhvm.adotai.common.vo.EnderecoVO;
import br.com.alevhvm.adotai.common.vo.RedeVO;
import br.com.alevhvm.adotai.ong.model.Ong;
import br.com.alevhvm.adotai.usuario.dto.UsuarioDTO;
import br.com.alevhvm.adotai.usuario.model.Usuario;
import br.com.alevhvm.adotai.usuario.repository.UsuarioRepository;
import br.com.alevhvm.adotai.usuario.validations.UsuarioValidacao;

@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioValidacao usuarioValidacao;

    @Mock
    private PagedResourcesAssembler<AdministradorDTO> assembler;

    @Mock
    private PasswordEncoder passwordEncoder;

    private RegistroDTO registroDTO;
    private UsuarioDTO usuarioDTO;
    private Usuario usuario;
    private Animal animalEntity;
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
        erros.add("Cell já está em usoo");

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
}
