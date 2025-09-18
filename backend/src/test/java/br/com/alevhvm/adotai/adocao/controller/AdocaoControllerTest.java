package br.com.alevhvm.adotai.adocao.controller;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alevhvm.adotai.adocao.dto.AdocaoDTO;
import br.com.alevhvm.adotai.adocao.enums.StatusAdocao;
import br.com.alevhvm.adotai.adocao.service.AdocaoService;
import br.com.alevhvm.adotai.auth.security.SecurityFilter;
import br.com.alevhvm.adotai.common.util.MediaType;
import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(
    controllers = AdocaoController.class,
    excludeFilters = @org.springframework.context.annotation.ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = SecurityFilter.class
    )
)
@AutoConfigureMockMvc(addFilters = false)
public class AdocaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AdocaoService adocaoService;

    private AdocaoDTO adocaoDTO;
    private AdocaoDTO adocaoCriada;
    private AdocaoDTO adocaoUpdate;
    private AdocaoDTO adocaoPartialUpdate;
    private Map<String, Object> updates;

    @BeforeEach
    void setUp() {
        adocaoDTO = new AdocaoDTO();
        adocaoDTO.setDataAdocao(LocalDate.parse("2024-07-11"));
        adocaoDTO.setStatus(StatusAdocao.APROVADA);

        adocaoCriada = new AdocaoDTO();
        adocaoCriada.setKey(2L);
        adocaoCriada.setDataAdocao(LocalDate.parse("2024-07-11"));
        adocaoCriada.setStatus(StatusAdocao.APROVADA);

        adocaoUpdate = new AdocaoDTO();
        adocaoUpdate.setDataAdocao(LocalDate.parse("2025-07-11"));
        adocaoUpdate.setStatus(StatusAdocao.CANCELADA);

        adocaoPartialUpdate = new AdocaoDTO();
        adocaoPartialUpdate.setStatus(StatusAdocao.CANCELADA);

        updates = new HashMap<>();
        updates = Map.of("nome", "Novo Nome");
    }

    @Test
    void deveRetornar200AoListarAdocoes() throws Exception {
        List<EntityModel<AdocaoDTO>> adocoes = List.of(EntityModel.of(adocaoDTO));

        PagedModel<EntityModel<AdocaoDTO>> pagedModel = PagedModel.of(
            adocoes,
            new PagedModel.PageMetadata(1, 0, 1)
        );

        when(adocaoService.findAll(any(Pageable.class))).thenReturn(pagedModel);

        mockMvc.perform(get("/api/v1/adocoes")
                .param("page", "0")
                .param("size", "10")
                .param("direction", "asc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.adocaoDTOList[0].dataAdocao").value("11/07/2024"))
                .andExpect(jsonPath("$._embedded.adocaoDTOList[0].status").value("APROVADA"));
    }

    @Test
    void deveRetornar200AoAcharAdocaoPorId() throws Exception {
        when(adocaoService.findById(1L)).thenReturn(adocaoDTO);

        mockMvc.perform(get("/api/v1/adocoes/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dataAdocao").value("11/07/2024"))
                .andExpect(jsonPath("$.status").value("APROVADA"));
    }

    @Test
    void deveRetornarNotFoundQuandoNaoAcharAdocaoPorId() throws Exception{
        when(adocaoService.findById(99L)).thenThrow(new EntityNotFoundException("Adoção não encontrada."));

        mockMvc.perform(get("/api/v1/adocoes/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Adoção não encontrada."));
    }

    @Test
    void deveRetornar201AoRegistrarAdocao() throws Exception {
        when(adocaoService.create(any(AdocaoDTO.class))).thenReturn(adocaoCriada);

        String json = "{"
            + "\"dataAdocao\":\"11/07/2024\","
            + "\"status\":\"APROVADA\""
            + "}";

        mockMvc.perform(post("/api/v1/adocoes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.dataAdocao").value("11/07/2024"))
                .andExpect(jsonPath("$.status").value("APROVADA"));
    }

    @Test
    void deveRetornar200AoAtualizarAdocao() throws Exception {
        when(adocaoService.update(any(AdocaoDTO.class), eq(1L))).thenReturn(adocaoUpdate);

        String json = "{"
            + "\"dataAdocao\":\"11/07/2025\","
            + "\"status\":\"CANCELADA\""
            + "}";

        mockMvc.perform(put("/api/v1/adocoes/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.dataAdocao").value("11/07/2025"))
                .andExpect(jsonPath("$.status").value("CANCELADA"));  
    }

    @Test
    void deveRetonarNotFoundAoAtualizarAdocaoInexistente() throws Exception {
        when(adocaoService.update(any(AdocaoDTO.class), eq(99L))).thenThrow(new EntityNotFoundException("Adoção não encontrada."));

        String json = "{"
            + "\"dataAdocao\":\"11/07/2025\","
            + "\"status\":\"CANCELADA\""
            + "}";

        mockMvc.perform(put("/api/v1/adocoes/{id}", 99L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Adoção não encontrada."));
    }

    @Test
    void deveRetornar200AoAtualizarParcialAdocao() throws Exception {
        when(adocaoService.partialUpdate(1L, updates)).thenReturn(adocaoPartialUpdate);

        mockMvc.perform(patch("/api/v1/adocoes/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELADA"));
    }

    @Test
    void deveRetornarNotFoundAoAtualizarParcialAdocaoInexistente() throws Exception {
        when(adocaoService.partialUpdate(99L, updates)).thenThrow(new EntityNotFoundException("Adoção não encontrada."));

        mockMvc.perform(patch("/api/v1/adocoes/{id}", 99L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Adoção não encontrada."));

    }

    @Test
    void deveRetornarNoContentAoDeletarAdocao() throws Exception {
        doNothing().when(adocaoService).delete(1L);

        mockMvc.perform(delete("/api/v1/adocoes/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarNotFoundAoDeletarAdocaoInexistente() throws Exception{
        doThrow(new EntityNotFoundException("Adoção não encontrada.")).when(adocaoService).delete(99L);

        mockMvc.perform(delete("/api/v1/adocoes/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Adoção não encontrada."));
    }
}
