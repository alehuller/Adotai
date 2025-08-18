package br.com.alevhvm.adotai.adocao.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
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

import br.com.alevhvm.adotai.adocao.dto.AdocaoDTO;
import br.com.alevhvm.adotai.adocao.enums.StatusAdocao;
import br.com.alevhvm.adotai.adocao.model.Adocao;
import br.com.alevhvm.adotai.adocao.repository.AdocaoRepository;
import br.com.alevhvm.adotai.animal.enums.StatusAnimal;
import br.com.alevhvm.adotai.animal.model.Animal;
import br.com.alevhvm.adotai.animal.repository.AnimalRepository;
import br.com.alevhvm.adotai.auth.enums.Roles;
import br.com.alevhvm.adotai.common.mapper.DozerMapper;
import br.com.alevhvm.adotai.common.vo.EnderecoVO;
import br.com.alevhvm.adotai.common.vo.RedeVO;
import br.com.alevhvm.adotai.ong.model.Ong;
import br.com.alevhvm.adotai.ong.repository.OngRepository;
import br.com.alevhvm.adotai.usuario.model.Usuario;
import br.com.alevhvm.adotai.usuario.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Validator;

@ExtendWith(MockitoExtension.class)
public class AdocaoServiceTest {

    @Mock
    private AdocaoRepository adocaoRepository;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private OngRepository ongRepository;

    @Mock
    private PagedResourcesAssembler<AdocaoDTO> assembler;

    @Mock
    private Validator validator;

    @InjectMocks
    private AdocaoService adocaoService;

    private AdocaoDTO adocaoDTO;
    private AdocaoDTO adocaoDiferente;
    private Adocao adocaoEntity;
    private Usuario usuario;
    private Ong ong;
    private Animal animal;

    private RedeVO redeVO;


    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1L);
        usuario.setEmail("teste@email.com");
        usuario.setNome("UserTeste");
        usuario.setNomeUsuario("UserTesteNome");
        usuario.setSenha("senha123");
        usuario.setCell("11999999999");
        usuario.setCpf("824.387.720-77");
        usuario.setRole(Roles.USER);

        redeVO = new RedeVO();
        redeVO.setInstagram("https://teste.adotai.com/");
        redeVO.setFacebook("https://teste.facebook.com/adotai");

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

        animal = new Animal();
        animal.setIdAnimal(1L);
        animal.setNome("Garfield");
        animal.setEspecie("Gato");
        animal.setRaca("Persa");
        animal.setDataNascimento(LocalDate.of(2021, 8, 3));
        animal.setPorte("Pequeno");
        animal.setSexo("Macho");
        animal.setStatus(StatusAnimal.INDISPONIVEL);
        animal.setOng(ong);
        animal.setFoto("foto.png");

        adocaoDTO = new AdocaoDTO();
        adocaoDTO.setKey(1L);
        adocaoDTO.setDataAdocao(LocalDate.parse("2024-07-11"));
        adocaoDTO.setStatus(StatusAdocao.APROVADA);
        adocaoDTO.setIdAnimal(1L);
        adocaoDTO.setIdUsuario(1L);

        adocaoEntity = new Adocao();
        adocaoEntity.setIdAdocao(1L);
        adocaoEntity.setDataAdocao(LocalDate.parse("2024-07-11"));
        adocaoEntity.setStatus(StatusAdocao.APROVADA);
        adocaoEntity.setAnimal(animal);
        adocaoEntity.setUsuario(usuario);

        adocaoDiferente = new AdocaoDTO();
        adocaoDiferente.setDataAdocao(LocalDate.parse("2024-07-10"));
        adocaoDiferente.setStatus(StatusAdocao.CANCELADA);
        adocaoDiferente.setIdAnimal(1L);;
        adocaoDiferente.setIdUsuario(1L);
    }

    @Test
    void deveCriarAdocaoComSucesso() {
        when(animalRepository.findById(adocaoDTO.getIdAnimal())).thenReturn(Optional.of(animal));
        when(usuarioRepository.findById(adocaoDTO.getIdUsuario())).thenReturn(Optional.of(usuario));
        when(adocaoRepository.save(any(Adocao.class))).thenReturn(adocaoEntity);

        AdocaoDTO resultado = adocaoService.create(adocaoDTO);

        assertNotNull(resultado);
        assertEquals(LocalDate.parse("2024-07-11"), resultado.getDataAdocao());
        assertEquals("Garfield", resultado.getNomeAnimal());
        verify(adocaoRepository).save(any(Adocao.class));
    }

    @Test
    void deveLancarExcecaoQuandoAdocaoEstiverNulaNaCriacao() {
        AdocaoDTO adocaoNula = null;
        
        assertThrows(NullPointerException.class, () -> {
            adocaoService.create(adocaoNula);
        });
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarAnimalNaBuscaPeloMesmoNaCriacao() {
        adocaoDTO.setIdAnimal(2L);
        when(animalRepository.findById(adocaoDTO.getIdAnimal())).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            adocaoService.create(adocaoDTO);
        });
       
        assertEquals("Animal não encontrado", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarUsuarioNaBuscaPeloMesmoNaCriacao() {
        adocaoDTO.setIdUsuario(2L);
        when(animalRepository.findById(adocaoDTO.getIdAnimal())).thenReturn(Optional.of(animal));
        when(usuarioRepository.findById(adocaoDTO.getIdUsuario())).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            adocaoService.create(adocaoDTO);
        });

        assertEquals("Usuário não encontrado", ex.getMessage());
    }

    @Test
    void deveRetornarPaginaDeAdocoesComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);

        List<Adocao> adocoes = List.of(adocaoEntity);
        Page<Adocao> adocaoPage = new PageImpl<>(adocoes, pageable, adocoes.size());

        when(adocaoRepository.findAll(pageable)).thenReturn(adocaoPage);

        AdocaoDTO dto = DozerMapper.parseObject(adocaoEntity, AdocaoDTO.class);
        List<AdocaoDTO> dtoList = List.of(dto);
        Page<AdocaoDTO> dtoPage = new PageImpl<>(dtoList, pageable, dtoList.size());

        PageMetadata metadata = new PageMetadata(10, 0, 1);
        PagedModel<EntityModel<AdocaoDTO>> pagedModelMock = PagedModel.empty(metadata);
        when(assembler.toModel(any(Page.class), any(Link.class))).thenReturn(pagedModelMock);

        PagedModel<EntityModel<AdocaoDTO>> resultado = adocaoService.findAll(pageable);

        assertNotNull(resultado);
        verify(adocaoRepository).findAll(pageable);
        verify(assembler).toModel(any(Page.class), any(Link.class));
    }

    @Test
    void deveRetornarPaginaVaziaQuandoNaoExistirAdocao() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Adocao> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(adocaoRepository.findAll(pageable)).thenReturn(emptyPage);

        PageMetadata metadata = new PageMetadata(10, 0, 0);
        PagedModel<EntityModel<AdocaoDTO>> pagedModelMock = PagedModel.empty(metadata);
        when(assembler.toModel(any(Page.class), any(Link.class))).thenReturn(pagedModelMock);

        PagedModel<EntityModel<AdocaoDTO>> resultado = adocaoService.findAll(pageable);

        assertNotNull(resultado);
        assertTrue(resultado.getContent().isEmpty());
    }

    @Test
    void deveRetornarAdocaoPeloIdProcuradoComSucesso() {
        when(adocaoRepository.findById(1L)).thenReturn(Optional.of(adocaoEntity));

        AdocaoDTO resultado = adocaoService.findById(1L);

        assertNotNull(resultado);
        assertEquals(LocalDate.parse("2024-07-11"), resultado.getDataAdocao());
        assertTrue(resultado.getLinks().hasLink("self"));

        verify(adocaoRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarAdocaoPorIdProcurado() {
        when(adocaoRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            adocaoService.findById(2L);
        });

        assertEquals("Adoção não encontrada.", ex.getMessage());
    }

    @Test
    void deveAtualizarAdocaoInteiroComSucesso() {
        when(adocaoRepository.findById(1L)).thenReturn(Optional.of(adocaoEntity));
        when(usuarioRepository.findById(adocaoDiferente.getIdUsuario())).thenReturn(Optional.of(usuario));
        when(animalRepository.findById(adocaoDiferente.getIdAnimal())).thenReturn(Optional.of(animal));
        when(ongRepository.findById(animal.getOng().getIdOng())).thenReturn(Optional.of(ong));
        when(adocaoRepository.save(any(Adocao.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AdocaoDTO resultado = adocaoService.update(adocaoDiferente, 1L);

        assertNotNull(resultado);
        assertEquals(LocalDate.parse("2024-07-10"), resultado.getDataAdocao());
        assertEquals(StatusAdocao.CANCELADA, resultado.getStatus());
        assertTrue(resultado.getLinks().hasLink("self"));

        verify(adocaoRepository).findById(1L);
        verify(usuarioRepository).findById(adocaoDiferente.getIdUsuario());
        verify(animalRepository).findById(adocaoDiferente.getIdAnimal());
        verify(ongRepository).findById(animal.getOng().getIdOng());
        verify(adocaoRepository).save(any(Adocao.class));
    }

    @Test
    void deveLancarExcecaoQuandoAdocaoEstiverNulaNoUpdate() {
        AdocaoDTO adocaoNula = null;

        assertThrows(NullPointerException.class, () -> {
            adocaoService.update(adocaoNula, 1L);
        });
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarAdocaoPeloIdInseridoNoUpdate() {
        when(adocaoRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            adocaoService.update(adocaoDiferente, 2L);
        });

        assertEquals("Adoção não encontrada.", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarUsuarioPeloIdInseridoNoUpdate() {
        adocaoDiferente.setIdUsuario(2L);
        when(adocaoRepository.findById(1L)).thenReturn(Optional.of(adocaoEntity));
        when(usuarioRepository.findById(adocaoDiferente.getIdUsuario())).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            adocaoService.update(adocaoDiferente, 1L);
        });

        assertEquals("Usuário não encontrada.", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarAnimalPeloIdInseridoNoUpdate() {
        adocaoDiferente.setIdAnimal(2L);
        when(adocaoRepository.findById(1L)).thenReturn(Optional.of(adocaoEntity));
        when(usuarioRepository.findById(adocaoDiferente.getIdUsuario())).thenReturn(Optional.of(usuario));
        when(animalRepository.findById(adocaoDiferente.getIdAnimal())).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            adocaoService.update(adocaoDiferente, 1L);
        });

        assertEquals("Animal não encontrado", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarOngPeloAnimalInseridoNoUpdate() {
        when(adocaoRepository.findById(1L)).thenReturn(Optional.of(adocaoEntity));
        when(usuarioRepository.findById(adocaoDiferente.getIdUsuario())).thenReturn(Optional.of(usuario));
        when(animalRepository.findById(adocaoDiferente.getIdAnimal())).thenReturn(Optional.of(animal));
        when(ongRepository.findById(animal.getOng().getIdOng())).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            adocaoService.update(adocaoDiferente, 1L);
        });

        assertEquals("Ong não encontrada.", ex.getMessage());
    }

    @Test
    void deveAtualizarAdocaoPartialComSucesso() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", "CANCELADA");

        when(adocaoRepository.findById(1L)).thenReturn(Optional.of(adocaoEntity));

        when(adocaoRepository.save(any(Adocao.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(validator.validate(any(AdocaoDTO.class))).thenReturn(Collections.emptySet());

        AdocaoDTO resultado = adocaoService.partialUpdate(1L, updates);

        assertNotNull(resultado);
        assertEquals(LocalDate.parse("2024-07-11"), resultado.getDataAdocao());
        assertEquals(StatusAdocao.CANCELADA, resultado.getStatus());
        assertTrue(resultado.getLinks().hasLink("self"));
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarAdocaoNoPartialUpdate() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", "CANCELADA");

        when(adocaoRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            adocaoService.partialUpdate(2L, updates);
        });

        assertEquals("Adoção não encontrada.", ex.getMessage());
    }

    @Test
    void deveDeletarAdocaoComSucesso() {
        when(adocaoRepository.existsById(1L)).thenReturn(true);

        adocaoService.delete(1L);

        verify(adocaoRepository).existsById(1L);
        verify(adocaoRepository).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarAdocaoNoDelete() {
        when(adocaoRepository.existsById(2L)).thenReturn(false);

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            adocaoService.delete(2L);
        });

        assertEquals("Adoção não encontrada.", ex.getMessage());
    }
}
