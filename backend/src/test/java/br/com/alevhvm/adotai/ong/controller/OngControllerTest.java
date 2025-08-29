package br.com.alevhvm.adotai.ong.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alevhvm.adotai.adocao.dto.AdocaoDTO;
import br.com.alevhvm.adotai.adocao.enums.StatusAdocao;
import br.com.alevhvm.adotai.animal.dto.AnimalDTO;
import br.com.alevhvm.adotai.animal.enums.StatusAnimal;
import br.com.alevhvm.adotai.animal.service.AnimalService;
import br.com.alevhvm.adotai.auth.security.SecurityFilter;
import br.com.alevhvm.adotai.common.util.MediaType;
import br.com.alevhvm.adotai.common.vo.CnpjVO;
import br.com.alevhvm.adotai.common.vo.EnderecoVO;
import br.com.alevhvm.adotai.ong.dto.OngDTO;
import br.com.alevhvm.adotai.ong.dto.OngFiltroDTO;
import br.com.alevhvm.adotai.ong.dto.OngUpdateDTO;
import br.com.alevhvm.adotai.ong.service.OngService;

@WebMvcTest(
    controllers = OngController.class,
    excludeFilters = @org.springframework.context.annotation.ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = SecurityFilter.class
    )
)
@AutoConfigureMockMvc(addFilters = false)
public class OngControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private OngService ongService;

    @MockitoBean
    private AnimalService animalService;

    private OngDTO ongDTO;
    private OngDTO ongUpdateDTO;
    private OngDTO ongPartialUpdate;
    private OngDTO ongFiltro;
    private AdocaoDTO adocaoDTO;
    private AnimalDTO animalDTO;

    private EnderecoVO enderecoVO;

    @BeforeEach
    void setUp() {
        enderecoVO = new EnderecoVO();
        enderecoVO.setNumero("111");
        enderecoVO.setCep("01111-011");

        ongDTO = new OngDTO();
        ongDTO.setKey(1L);
        ongDTO.setNome("Nome Teste");
        ongDTO.setNomeUsuario("NomeUsuarioTeste");
        ongDTO.setFotoPerfil("FotoTeste");
        ongDTO.setEmail("email@teste.com");
        ongDTO.setSenha("senha123");
        ongDTO.setEndereco(enderecoVO);
        ongDTO.setCell("(11) 91111-1111");
        ongDTO.setCnpj(new CnpjVO("30.681.527/0001-90"));
        ongDTO.setResponsavel("Responsavel Teste");
        ongDTO.setDescricao("Descricao Teste");

        ongUpdateDTO = new OngDTO();
        ongUpdateDTO.setNome("Nome Teste Update");
        ongUpdateDTO.setNomeUsuario("NomeUsuarioTesteUpdate");
        ongUpdateDTO.setFotoPerfil("FotoTesteUpdate");
        ongUpdateDTO.setEmail("email@testeupdate.com");
        ongUpdateDTO.setSenha("senha123");
        ongUpdateDTO.setEndereco(enderecoVO);
        ongUpdateDTO.setCell("(11) 91111-1111");
        ongUpdateDTO.setResponsavel("Responsavel Teste Update");
        ongUpdateDTO.setDescricao("Descricao Teste Update");

        ongPartialUpdate = new OngDTO();
        ongPartialUpdate.setNome("Nome Teste Partial");

        ongFiltro = new OngDTO();
        ongFiltro.setNome("Nome Teste Filtro");
        ongFiltro.setEmail("email@testefiltro.com");
        ongFiltro.setDescricao("Descricao Teste Filtro");

        adocaoDTO = new AdocaoDTO();
        adocaoDTO.setKey(1L);
        adocaoDTO.setDataAdocao(LocalDate.parse("2024-07-11"));
        adocaoDTO.setStatus(StatusAdocao.APROVADA);

        animalDTO = new AnimalDTO();
        animalDTO.setKey(1L);
        animalDTO.setNome("Nome Teste Animal");
        animalDTO.setEspecie("Especie Teste");
        animalDTO.setRaca("Raca Teste");
        animalDTO.setDataNascimento(LocalDate.parse("2024-07-11"));
        animalDTO.setPorte("Porte Teste");
        animalDTO.setSexo("Sexo Teste");
        animalDTO.setStatus(StatusAnimal.DISPONIVEL);
        animalDTO.setIdOng(1L);
    }

    @Test
    void deveListarOngs() throws Exception {
        List<EntityModel<OngDTO>> ongs = List.of(EntityModel.of(ongDTO));

        PagedModel<EntityModel<OngDTO>> pagedModel = PagedModel.of(
            ongs,
            new PagedModel.PageMetadata(1, 0, 1)
        );

        when(ongService.findAll(any(Pageable.class))).thenReturn(pagedModel);

        mockMvc.perform(get("/api/v1/ongs")
                .param("page", "0")
                .param("size", "10")
                .param("direction", "asc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.ongDTOList[0].nome").value("Nome Teste"))
                .andExpect(jsonPath("$._embedded.ongDTOList[0].email").value("email@teste.com"))
                .andExpect(jsonPath("$._embedded.ongDTOList[0].descricao").value("Descricao Teste"));
    }

    @Test
    void deveAcharOngPorId() throws Exception {
        when(ongService.findById(1L)).thenReturn(ongDTO);

        mockMvc.perform(get("/api/v1/ongs/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Teste"))
                .andExpect(jsonPath("$.email").value("email@teste.com"))
                .andExpect(jsonPath("$.descricao").value("Descricao Teste"));
    }

    @Test
    void deveAcharOngPorNome() throws Exception {
        when(ongService.findByNomeUsuario("Nome Teste")).thenReturn(ongDTO);

        mockMvc.perform(get("/api/v1/ongs/nomeUsuario/{nomeUsuario}", "Nome Teste")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Teste"))
                .andExpect(jsonPath("$.email").value("email@teste.com"))
                .andExpect(jsonPath("$.descricao").value("Descricao Teste"));
    }

    @Test
    void deveListarAdocoesPorId() throws Exception{
        List<EntityModel<AdocaoDTO>> adocoes = List.of(EntityModel.of(adocaoDTO));

        PagedModel<EntityModel<AdocaoDTO>> pagedModel = PagedModel.of(
            adocoes,
            new PagedModel.PageMetadata(1, 0, 1)
        );

        when(ongService.findAllAdocoesByOngId(eq(1L), any(Pageable.class))).thenReturn(pagedModel);

        mockMvc.perform(get("/api/v1/ongs/{id}/adocoes", 1l)
                .param("page", "0")
                .param("size", "10")
                .param("direction", "asc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.adocaoDTOList[0].dataAdocao").value("11/07/2024"))
                .andExpect(jsonPath("$._embedded.adocaoDTOList[0].status").value("APROVADA"));
    }

    @Test
    void deveListarAnimaisDeUmaOng() throws Exception {
        List<EntityModel<AnimalDTO>> animais = List.of(EntityModel.of(animalDTO));

        PagedModel<EntityModel<AnimalDTO>> pagedModel = PagedModel.of(
            animais,
            new PagedModel.PageMetadata(1, 0, 1)
        );

        when(animalService.findAllByOngNome(eq("Nome Teste"), any(Pageable.class))).thenReturn(pagedModel);

        mockMvc.perform(get("/api/v1/ongs/{nomeUsuario}/animais", "Nome Teste")
                .param("page", "0")
                .param("size", "10")
                .param("direction", "asc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.animalDTOList[0].nome").value("Nome Teste Animal"))
                .andExpect(jsonPath("$._embedded.animalDTOList[0].raca").value("Raca Teste"))
                .andExpect(jsonPath("$._embedded.animalDTOList[0].status").value("DISPONIVEL"));
    }

    @Test
    void deveFiltrarOngs() throws Exception {
        OngFiltroDTO filtro = new OngFiltroDTO();
        filtro.setNome("Nome Teste Filtro");

        Page<OngDTO> pagedResult = new PageImpl<>(List.of(ongFiltro));

        when(ongService.filtrarOngs(any(OngFiltroDTO.class), any(Pageable.class))).thenReturn(pagedResult);

        mockMvc.perform(post("/api/v1/ongs/filtro")
                .param("page", "0")
                .param("size", "10")
                .param("direction", "asc")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(filtro)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Nome Teste Filtro"))
                .andExpect(jsonPath("$.content[0].email").value("email@testefiltro.com"))
                .andExpect(jsonPath("$.content[0].descricao").value("Descricao Teste Filtro"));
    }

    @Test
    void deveRegistrarOng() throws Exception{
        when(ongService.create(any(OngDTO.class))).thenReturn(ongDTO);

        String json = "{"
        + "\"nome\":\"Nome Teste\","
        + "\"nomeUsuario\":\"NomeUsuarioTeste\","
        + "\"fotoPerfil\":\"FotoTeste\","
        + "\"email\":\"email@teste.com\","
        + "\"senha\":\"senha123\","
        + "\"endereco\":{"
        +     "\"numero\":\"111\","
        +     "\"cep\":\"01111-011\""
        + "},"
        + "\"cell\":\"(11) 91111-1111\","
        + "\"cnpj\":\"30.681.527/0001-90\","
        + "\"responsavel\":\"Responsavel Teste\","
        + "\"descricao\":\"Descricao Teste\""
        + "}";


            mockMvc.perform(post("/api/v1/ongs")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("Location"))
                    .andExpect(jsonPath("$.nome").value("Nome Teste"))
                    .andExpect(jsonPath("$.email").value("email@teste.com"))
                    .andExpect(jsonPath("$.descricao").value("Descricao Teste"));
    }

    @Test
    void deveAtualizarOng() throws Exception {
        when(ongService.update(any(OngUpdateDTO.class), eq("Nome Teste"))).thenReturn(ongUpdateDTO);

        String json = "{"
        + "\"nome\":\"Nome Teste Update\","
        + "\"nomeUsuario\":\"NomeUsuarioTesteUpdate\","
        + "\"fotoPerfil\":\"FotoTesteUpdate\","
        + "\"email\":\"email@testeupdate.com\","
        + "\"senha\":\"senha123\","
        + "\"endereco\":{"
        +     "\"numero\":\"111\","
        +     "\"cep\":\"01111-011\""
        + "},"
        + "\"cell\":\"(11) 91111-1111\","
        + "\"responsavel\":\"Responsavel Teste Update\","
        + "\"descricao\":\"Descricao Teste Update\""
        + "}";


        mockMvc.perform(put("/api/v1/ongs/{nomeUsuario}", "Nome Teste")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Teste Update"))
                .andExpect(jsonPath("$.email").value("email@testeupdate.com"))
                .andExpect(jsonPath("$.descricao").value("Descricao Teste Update")); 
    }

    @Test
    void deveAtualizarParcialOng() throws Exception {
        Map<String, Object> updates = Map.of("nome", "Nome Teste Partial");
        when(ongService.partialUpdate("Nome Teste", updates)).thenReturn(ongPartialUpdate);

        mockMvc.perform(patch("/api/v1/ongs/{nomeUsuario}", "Nome Teste")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Teste Partial"));
    }

    @Test
    void deveDeletarOng() throws Exception {
        doNothing().when(ongService).delete("Nome Teste");

        mockMvc.perform(delete("/api/v1/ongs/{nomeUsuario}", "Nome Teste"))
                .andExpect(status().isNoContent());
    }
}
