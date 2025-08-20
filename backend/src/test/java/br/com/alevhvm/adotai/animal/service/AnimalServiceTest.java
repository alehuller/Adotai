package br.com.alevhvm.adotai.animal.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;

import br.com.alevhvm.adotai.administrador.dto.AdministradorDTO;
import br.com.alevhvm.adotai.animal.dto.AnimalDTO;
import br.com.alevhvm.adotai.animal.dto.AnimalFiltroDTO;
import br.com.alevhvm.adotai.animal.enums.StatusAnimal;
import br.com.alevhvm.adotai.animal.model.Animal;
import br.com.alevhvm.adotai.animal.repository.AnimalRepository;
import br.com.alevhvm.adotai.auth.enums.Roles;
import br.com.alevhvm.adotai.common.mapper.DozerMapper;
import br.com.alevhvm.adotai.common.vo.DescricaoVO;
import br.com.alevhvm.adotai.common.vo.EnderecoVO;
import br.com.alevhvm.adotai.common.vo.RedeVO;
import br.com.alevhvm.adotai.ong.model.Ong;
import br.com.alevhvm.adotai.ong.repository.OngRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Validator;

@ExtendWith(MockitoExtension.class)
public class AnimalServiceTest {

    @InjectMocks
    private AnimalService animalService;

    @Mock
    private OngRepository ongRepository;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private PagedResourcesAssembler<AdministradorDTO> assembler;

    @Mock
    private Validator validator;

    private AnimalDTO animalDTO;
    private AnimalDTO animalDiferente;
    private Animal animalEntity;
    private Ong ong;

    private DescricaoVO descricaoVO;
    private RedeVO redeVO;
    private AnimalFiltroDTO filtro;

    @BeforeEach
    void setUp() {
        filtro = new AnimalFiltroDTO();

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

        descricaoVO = new DescricaoVO();
        descricaoVO.setGeral("Descrição Teste");
        descricaoVO.setHistoricoSaude("Historico Teste");
        descricaoVO.setVacinacao("Vacinacao Teste");

        animalDTO = new AnimalDTO();
        animalDTO.setKey(1L);
        animalDTO.setNome("Animal Nome");
        animalDTO.setEspecie("Cachorro");
        animalDTO.setRaca("Spitz Alemão");
        animalDTO.setDataNascimento(LocalDate.parse("2024-07-11"));
        animalDTO.setFoto("Foto Teste");
        animalDTO.setDescricao(descricaoVO);
        animalDTO.setPorte("Porte Teste");
        animalDTO.setSexo("Macho");
        animalDTO.setStatus(StatusAnimal.ADOTADO);
        animalDTO.setIdOng(1L);

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

        animalDiferente = new AnimalDTO();
        animalDiferente.setNome("Animal Nome Diferente");
        animalDiferente.setEspecie("Gato");
        animalDiferente.setRaca("Siames");
        animalDiferente.setDataNascimento(LocalDate.parse("2024-07-20"));
        animalDiferente.setFoto("Foto Teste Gato");
        animalDiferente.setDescricao(descricaoVO);
        animalDiferente.setPorte("Porte Teste Gato");
        animalDiferente.setSexo("Femea");
        animalDiferente.setStatus(StatusAnimal.ADOTADO);
        animalDiferente.setIdOng(1L);
    }

    @Test
    void deveCriarAnimalComSucesso() {
        when(ongRepository.findById(animalDTO.getIdOng())).thenReturn(Optional.of(ong));
        when(animalRepository.save(any(Animal.class))).thenReturn(animalEntity);

        AnimalDTO resultado = animalService.create(animalDTO);

        assertNotNull(resultado);
        assertEquals("Animal Nome", resultado.getNome());
        assertEquals("Cachorro", resultado.getEspecie());
        assertEquals("Spitz Alemão", resultado.getRaca());
        verify(ongRepository).findById(animalDTO.getIdOng());
        verify(animalRepository).save(any(Animal.class));
    }

    @Test
    void deveLancarExcecaoQuandoAnimalEstiverNuloNaCriacao() {
        AnimalDTO animalNulo = null;

        assertThrows(NullPointerException.class, () -> {
            animalService.create(animalNulo);
        });

        verify(animalRepository, never()).save(any(Animal.class));
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarOngNoAnimalInseridoNaCriacao() {
        when(ongRepository.findById(animalDTO.getIdOng())).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            animalService.create(animalDTO);
        });

        assertEquals("Ong não encontrada", ex.getMessage());
        verify(animalRepository, never()).save(any(Animal.class));
    }

    @Test
    void deveAdicionarLinkSelfNoDTORetornado() {
        when(ongRepository.findById(animalDTO.getIdOng())).thenReturn(Optional.of(ong));
        when(animalRepository.save(any(Animal.class))).thenReturn(animalEntity);

        AnimalDTO resultado = animalService.create(animalDTO);

        assertFalse(resultado.getLinks().isEmpty());
        assertTrue(resultado.getLinks().stream().anyMatch(link -> link.getRel().value().equals("self")));
    }

    @Test
    void deveRetornarPaginaDeAnimalComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);

        List<Animal> animais = List.of(animalEntity);
        Page<Animal> animalPage = new PageImpl<>(animais, pageable, animais.size());

        when(animalRepository.findAll(pageable)).thenReturn(animalPage);

        AnimalDTO dto = DozerMapper.parseObject(animalEntity, AnimalDTO.class);
        List<AnimalDTO> dtoList = List.of(dto);
        Page<AnimalDTO> dtoPage = new PageImpl<>(dtoList, pageable, dtoList.size());

        PageMetadata metadata = new PageMetadata(10, 0, 1);
        PagedModel<EntityModel<AnimalDTO>> pagedModelMock = PagedModel.empty(metadata);
        when(assembler.toModel(any(Page.class), any(Link.class))).thenReturn(pagedModelMock);

        PagedModel<EntityModel<AnimalDTO>> resultado = animalService.findAll(pageable);

        assertNotNull(resultado);
        verify(animalRepository).findAll(pageable);
        verify(assembler).toModel(any(Page.class), any(Link.class));
    }

    @Test
    void deveRetornarPaginaVaziaQuandoNaoExistirAnimais() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Animal> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(animalRepository.findAll(pageable)).thenReturn(emptyPage);

        PageMetadata metadata = new PageMetadata(10, 0, 0);
        PagedModel<EntityModel<AnimalDTO>> pagedModelMock = PagedModel.empty(metadata);
        when(assembler.toModel(any(Page.class), any(Link.class))).thenReturn(pagedModelMock);

        PagedModel<EntityModel<AnimalDTO>> resultado = animalService.findAll(pageable);

        assertNotNull(resultado);
        assertTrue(resultado.getContent().isEmpty());
    }

    @Test
    void deveRetornarPaginaDeAnimaisPorOngNomeComSucesso() {
        Pageable pageable = PageRequest.of(0, 10);

        List<Animal> animais = List.of(animalEntity);
        Page<Animal> animalPage = new PageImpl<>(animais, pageable, animais.size());

        when(animalRepository.findByOngNomeUsuario("amigosanimais", pageable)).thenReturn(animalPage);

        AnimalDTO dto = DozerMapper.parseObject(animalEntity, AnimalDTO.class);
        List<AnimalDTO> dtoList = List.of(dto);
        Page<AnimalDTO> dtoPage = new PageImpl<>(dtoList, pageable, dtoList.size());

        PageMetadata metadata = new PageMetadata(10, 0, 1);
        PagedModel<EntityModel<AnimalDTO>> pagedModelMock = PagedModel.empty(metadata);
        when(assembler.toModel(any(Page.class), any(Link.class))).thenReturn(pagedModelMock);

        PagedModel<EntityModel<AnimalDTO>> resultado = animalService.findAllByOngNome("amigosanimais", pageable);

        assertNotNull(resultado);
        verify(animalRepository).findByOngNomeUsuario("amigosanimais", pageable);
        verify(assembler).toModel(any(Page.class), any(Link.class));
    }

    @Test
    void deveRetornarPaginaDeAnimaisPorOngNomeVazia() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Animal> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(animalRepository.findByOngNomeUsuario("nomeErrado", pageable)).thenReturn(emptyPage);

        PageMetadata metadata = new PageMetadata(10, 0, 0);
        PagedModel<EntityModel<AnimalDTO>> pagedModelMock = PagedModel.empty(metadata);
        when(assembler.toModel(any(Page.class), any(Link.class))).thenReturn(pagedModelMock);

        PagedModel<EntityModel<AnimalDTO>> resultado = animalService.findAllByOngNome("nomeErrado", pageable);

        assertNotNull(resultado);
        assertTrue(resultado.getContent().isEmpty());
    }

    @Test
    void deveRetornarAnimalPeloIdProcuradoComSucesso() {
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animalEntity));

        AnimalDTO resultado = animalService.findById(1L);

        assertNotNull(resultado);
        assertEquals("Animal Nome", resultado.getNome());
        assertEquals("Cachorro", resultado.getEspecie());
        assertEquals("Spitz Alemão", resultado.getRaca());
        assertTrue(resultado.getLinks().hasLink("self"));
        assertTrue(resultado.getRequiredLink("self").getHref().endsWith("/api/v1/animais/" + animalEntity.getIdAnimal()));

        verify(animalRepository, times(1)).findById(1L);
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarAnimalPorIdProcurado() {
        when(animalRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            animalService.findById(2L);
        });

        assertEquals("Animal não encontrado.", ex.getMessage());
    }

    @Test
    void deveRetornarAnimalPeloNomeUsuarioProcuradoComSucesso() {
        when(animalRepository.findByNome("Animal Nome")).thenReturn(Optional.of(animalEntity));

        AnimalDTO resultado = animalService.findByNome("Animal Nome");

        assertNotNull(resultado);
        assertEquals("Animal Nome", resultado.getNome());
        assertEquals("Cachorro", resultado.getEspecie());
        assertEquals("Spitz Alemão", resultado.getRaca());
        assertTrue(resultado.getLinks().hasLink("self"));

        verify(animalRepository, times(1)).findByNome("Animal Nome");
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarAnimalPorNomeUsuarioProcurado() {
        when(animalRepository.findByNome("Animal Nome Errado")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            animalService.findByNome("Animal Nome Errado");
        });

        assertEquals("Animal não encontrado.", ex.getMessage());
    }

    @Test
    void deveRetornarOsAnimaisPeloFiltro() {
        filtro.setNome("Animal Nome");
        Pageable pageable = PageRequest.of(0, 10);

        List<Animal> animais = List.of(animalEntity);
        Page<Animal> animalPage = new PageImpl<>(animais, pageable, animais.size());

        when(animalRepository.filtrarAnimaisNativo(filtro, pageable)).thenReturn(animalPage);

        Page<AnimalDTO> resultado = animalService.filtrarAnimais(filtro, pageable);

        assertNotNull(resultado);
        assertEquals(1, resultado.getTotalElements());
        assertEquals("Animal Nome", resultado.getContent().get(0).getNome());

        verify(animalRepository).filtrarAnimaisNativo(filtro, pageable);
    }

    @Test
    void deveRetornarPaginaVazioDeAnimaisPeloFiltro() {
        filtro.setNome("Animal Nome Inexistente");
        Pageable pageable = PageRequest.of(0, 10);
        Page<Animal> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

        when(animalRepository.filtrarAnimaisNativo(filtro, pageable)).thenReturn(emptyPage);

        Page<AnimalDTO> resultado = animalService.filtrarAnimais(filtro, pageable);

        assertNotNull(resultado);
        assertTrue(resultado.getContent().isEmpty());
    }

    @Test
    void deveAtualizarAnimalInteiroComSucesso() {
        when(animalRepository.findByNome("Animal Nome")).thenReturn(Optional.of(animalEntity));
        when(ongRepository.findById(animalDiferente.getIdOng())).thenReturn(Optional.of(ong));
        when(animalRepository.save(any(Animal.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AnimalDTO resultado = animalService.update(animalDiferente, "Animal Nome");

        assertNotNull(resultado);
        assertEquals("Animal Nome Diferente", resultado.getNome());
        assertEquals("Gato", resultado.getEspecie());
        assertEquals("Siames", resultado.getRaca());
        assertTrue(resultado.getLinks().hasLink("self"));

        verify(animalRepository).findByNome("Animal Nome");
        verify(ongRepository).findById(animalDTO.getIdOng());
        verify(animalRepository).save(any(Animal.class));
    }

    @Test
    void deveLancarExcecaoQuandoAnimalEstiverNuloNoUpdate() {
        AnimalDTO animalNulo = null;

        assertThrows(NullPointerException.class, () -> {
            animalService.update(animalNulo, "Animal Nome");
        });

        verify(animalRepository, never()).save(any(Animal.class));
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarAnimalPorNomeNoUpdate() {
        when(animalRepository.findByNome("Animal Nome Errado")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            animalService.update(animalDiferente, "Animal Nome Errado");
        });

        assertEquals("Animal não encontrado.", ex.getMessage());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarOngDoAnimalNoUpdate() {
        when(animalRepository.findByNome("Animal Nome")).thenReturn(Optional.of(animalEntity));
        when(ongRepository.findById(animalDiferente.getIdOng())).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            animalService.update(animalDiferente, "Animal Nome");
        });

        assertEquals("Ong não encontrada.", ex.getMessage());
    }

    @Test
    void deveAtualizarAnimalPartialComSucesso() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("nome", "NovoNome");
        updates.put("raca", "York");

        when(animalRepository.findByNome("Animal Nome")).thenReturn(Optional.of(animalEntity));

        when(animalRepository.save(any(Animal.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(validator.validate(any(AnimalDTO.class))).thenReturn(Collections.emptySet());

        AnimalDTO resultado = animalService.partialUpdate("Animal Nome", updates);

        assertNotNull(resultado);
        assertEquals("NovoNome", resultado.getNome());
        assertEquals("York", resultado.getRaca());
        assertEquals("Cachorro", resultado.getEspecie());
        assertTrue(resultado.getLinks().hasLink("self"));
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarAnimalNoPartialUpdate() {
        Map<String, Object> updates = new HashMap<>();
        updates.put("nome", "NovoNome");

        when(animalRepository.findByNome("Animal Nome Errado")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            animalService.partialUpdate("Animal Nome Errado", updates);
        });

        assertEquals("Animal não encontrado.", ex.getMessage());
    }

    @Test
    void deveDeletarAnimalComSucesso() {
        when(animalRepository.findByNome("Animal Nome")).thenReturn(Optional.of(animalEntity));

        animalService.delete("Animal Nome");

        verify(animalRepository).findByNome("Animal Nome");
        verify(animalRepository).deleteByNome("Animal Nome");
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarAnimalNoDelete() {
        when(animalRepository.findByNome("Animal Nome Errado")).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> {
            animalService.delete("Animal Nome Errado");
        });

        assertEquals("Animal não encontrado", ex.getMessage());
    }
}
