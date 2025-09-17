package br.com.alevhvm.adotai.animal.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
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

import br.com.alevhvm.adotai.animal.dto.AnimalDTO;
import br.com.alevhvm.adotai.animal.dto.AnimalFiltroDTO;
import br.com.alevhvm.adotai.animal.enums.StatusAnimal;
import br.com.alevhvm.adotai.animal.service.AnimalService;
import br.com.alevhvm.adotai.auth.security.SecurityFilter;
import br.com.alevhvm.adotai.common.util.MediaType;
import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(
    controllers = AnimalController.class,
    excludeFilters = @org.springframework.context.annotation.ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = SecurityFilter.class
    )
)
@AutoConfigureMockMvc(addFilters = false)
public class AnimalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AnimalService animalService;

    private AnimalDTO animalDTO;
    private AnimalDTO animalUpdate;
    private AnimalDTO animalPartialUpdate;
    private AnimalDTO animalFiltro;

    @BeforeEach
    void setUp() {
        animalDTO = new AnimalDTO();
        animalDTO.setKey(1L);
        animalDTO.setNome("Nome Teste");
        animalDTO.setEspecie("Especie Teste");
        animalDTO.setRaca("Raca Teste");
        animalDTO.setDataNascimento(LocalDate.parse("2024-07-11"));
        animalDTO.setPorte("Porte Teste");
        animalDTO.setSexo("Sexo Teste");
        animalDTO.setStatus(StatusAnimal.DISPONIVEL);
        animalDTO.setIdOng(1L);

        animalUpdate = new AnimalDTO();
        animalUpdate.setNome("Nome Teste Update");
        animalUpdate.setEspecie("Especie Teste Update");
        animalUpdate.setRaca("Raca Teste Update");
        animalUpdate.setDataNascimento(LocalDate.parse("2025-07-11"));
        animalUpdate.setPorte("Porte Teste Update");
        animalUpdate.setSexo("Sexo Teste Update");

        animalPartialUpdate = new AnimalDTO();
        animalPartialUpdate.setNome("Nome Teste Partial");

        animalFiltro = new AnimalDTO();
        animalFiltro.setNome("Nome Teste Filtro");
        animalFiltro.setEspecie("Especie Teste");
        animalFiltro.setRaca("Raca Teste");
    }

    @Test
    void deveListarAnimais() throws Exception {
        List<EntityModel<AnimalDTO>> animais = List.of(EntityModel.of(animalDTO));

        PagedModel<EntityModel<AnimalDTO>> pagedModel = PagedModel.of(
            animais,
            new PagedModel.PageMetadata(1, 0, 1)
        );

        when(animalService.findAll(any(Pageable.class))).thenReturn(pagedModel);

        mockMvc.perform(get("/api/v1/animais")
                .param("page", "0")
                .param("size", "10")
                .param("direction", "asc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.animalDTOList[0].nome").value("Nome Teste"))
                .andExpect(jsonPath("$._embedded.animalDTOList[0].especie").value("Especie Teste"))
                .andExpect(jsonPath("$._embedded.animalDTOList[0].raca").value("Raca Teste"));
    }

    @Test
    void deveAcharAnimalPorId() throws Exception {
        when(animalService.findById(1L)).thenReturn(animalDTO);

        mockMvc.perform(get("/api/v1/animais/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Teste"))
                .andExpect(jsonPath("$.especie").value("Especie Teste"))
                .andExpect(jsonPath("$.raca").value("Raca Teste"));
    }

    @Test
    void deveRetornarNotFoundAoNaoAcharAnimalPorId() throws Exception {
        when(animalService.findById(99L)).thenThrow(new EntityNotFoundException("Animal não encontrado."));

        mockMvc.perform(get("/api/v1/animais/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Animal não encontrado."));
    }

    @Test
    void deveAcharAnimalPorNome() throws Exception {
        when(animalService.findByNome("Nome Teste")).thenReturn(animalDTO);

        mockMvc.perform(get("/api/v1/animais/nome/{nome}", "Nome Teste")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Teste"))
                .andExpect(jsonPath("$.especie").value("Especie Teste"))
                .andExpect(jsonPath("$.raca").value("Raca Teste"));
    }

    @Test
    void deveRetornarNotFoundAoNaoAcharAnimalPorNome() throws Exception {
        when(animalService.findByNome("NomeErrado")).thenThrow(new EntityNotFoundException("Animal não encontrado."));

        mockMvc.perform(get("/api/v1/animais/nome/{nome}", "NomeErrado"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Animal não encontrado."));
    }

    @Test
    void deveFiltrarAnimais() throws Exception {
        AnimalFiltroDTO filtro = new AnimalFiltroDTO();
        filtro.setNome("Nome Teste Filtro");

        Page<AnimalDTO> pagedResult = new PageImpl<>(List.of(animalFiltro));

        when(animalService.filtrarAnimais(any(AnimalFiltroDTO.class), any(Pageable.class))).thenReturn(pagedResult);

        mockMvc.perform(post("/api/v1/animais/filtro")
                .param("page", "0")
                .param("size", "10")
                .param("direction", "asc")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(filtro)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Nome Teste Filtro"))
                .andExpect(jsonPath("$.content[0].especie").value("Especie Teste"))
                .andExpect(jsonPath("$.content[0].raca").value("Raca Teste"));
    }

    @Test
    void deveRegistrarAnimal() throws Exception{
        when(animalService.create(any(AnimalDTO.class))).thenReturn(animalDTO);

        String json = "{"
            + "\"nome\":\"Nome Teste\","
            + "\"especie\":\"Especie Teste\","
            + "\"raca\":\"Raca Teste\","
            + "\"dataNascimento\":\"11/07/2024\","
            + "\"porte\":\"Porte Teste\","
            + "\"sexo\":\"Sexo Teste\","
            + "\"status\":\"DISPONIVEL\","
            + "\"idOng\":1"
            + "}";

            mockMvc.perform(post("/api/v1/animais")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(json))
                    .andExpect(status().isCreated())
                    .andExpect(header().exists("Location"))
                    .andExpect(jsonPath("$.nome").value("Nome Teste"))
                    .andExpect(jsonPath("$.especie").value("Especie Teste"))
                    .andExpect(jsonPath("$.sexo").value("Sexo Teste"));
    }

    @Test
    void deveAtualizarAnimal() throws Exception {
        when(animalService.update(any(AnimalDTO.class), eq("Nome Teste"))).thenReturn(animalUpdate);

        String json = "{"
            + "\"nome\":\"Nome Teste Update\","
            + "\"especie\":\"Especie Teste Update\","
            + "\"raca\":\"Raca Teste Update\","
            + "\"dataNascimento\":\"11/07/2025\","
            + "\"porte\":\"Porte Teste Update\","
            + "\"sexo\":\"Sexo Teste Update\""
            + "}";

        mockMvc.perform(put("/api/v1/animais/{nome}", "Nome Teste")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Teste Update"))
                .andExpect(jsonPath("$.especie").value("Especie Teste Update"))
                .andExpect(jsonPath("$.sexo").value("Sexo Teste Update")); 
    }

    @Test
    void deveRetornarNotFoundAoAtualizarAnimalInexistente() throws Exception{
        String json = "{"
            + "\"nome\":\"Nome Teste Update\","
            + "\"especie\":\"Especie Teste Update\","
            + "\"raca\":\"Raca Teste Update\","
            + "\"dataNascimento\":\"11/07/2025\","
            + "\"porte\":\"Porte Teste Update\","
            + "\"sexo\":\"Sexo Teste Update\""
            + "}";

        when(animalService.update(any(AnimalDTO.class), eq("AnimalInexistente"))).thenThrow(new EntityNotFoundException("Animal não encontrado."));

        mockMvc.perform(put("/api/v1/animais/{nome}", "AnimalInexistente")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Animal não encontrado."));
    }

    @Test
    void deveAtualizarParcialAnimal() throws Exception {
        Map<String, Object> updates = Map.of("nome", "Nome Teste Partial");
        when(animalService.partialUpdate("Nome Teste", updates)).thenReturn(animalPartialUpdate);

        mockMvc.perform(patch("/api/v1/animais/{nome}", "Nome Teste")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Teste Partial"));
    }

    @Test
    void deveRetornarNotFoundAoAtualizarParcialAnimalInexistente() throws Exception {
        Map<String, Object> updates = Map.of("nome", "Novo Nome");
        when(animalService.partialUpdate("animalTesteErrado", updates)).thenThrow(new EntityNotFoundException("Animal não encontrado."));

        mockMvc.perform(patch("/api/v1/animais/{nome}", "animalTesteErrado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updates)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Animal não encontrado."));
    }

    @Test
    void deveDeletarAnimal() throws Exception {
        doNothing().when(animalService).delete("Nome Teste");

        mockMvc.perform(delete("/api/v1/animais/{nome}", "Nome Teste"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarNotFoundAoDeletarAnimalInexistente() throws Exception {
        doThrow(new EntityNotFoundException("Animal não encontrado")).when(animalService).delete("naoExistente");

        mockMvc.perform(delete("/api/v1/animais/{nome}", "naoExistente"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Animal não encontrado"));
    }
}
