package br.com.alevhvm.adotai.administrador.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

import br.com.alevhvm.adotai.administrador.dto.AdministradorDTO;
import br.com.alevhvm.adotai.administrador.service.AdministradorService;
import br.com.alevhvm.adotai.auth.security.SecurityFilter;
import br.com.alevhvm.adotai.common.util.MediaType;
import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(
    controllers = AdministradorController.class,
    excludeFilters = @org.springframework.context.annotation.ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = SecurityFilter.class
    )
)
@AutoConfigureMockMvc(addFilters = false) 
public class AdministradorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AdministradorService administradorService;

    private AdministradorDTO administradorDTO;
    private AdministradorDTO administradorUpdate;
    private AdministradorDTO administradorPartialUpdate;
    private Map<String, Object> updates;

    @BeforeEach
    void setUp() {
        administradorDTO = new AdministradorDTO();
        administradorDTO.setNome("Administrador Teste");
        administradorDTO.setNomeUsuario("adminTeste");
        administradorDTO.setEmail("admin@teste.com");

        administradorUpdate = new AdministradorDTO();
        administradorUpdate.setNome("Administrador Teste Diferente");
        administradorUpdate.setNomeUsuario("adminTesteDiferente");
        administradorUpdate.setEmail("admindiferente@teste.com");
        administradorUpdate.setSenha("senha123");
        administradorUpdate.setCell("(11) 91234-5678");
        administradorUpdate.setFotoPerfil("foto.png");

        administradorPartialUpdate = new AdministradorDTO();
        administradorPartialUpdate.setNome("Novo Nome");

        updates = new HashMap<>();
        updates = Map.of("nome", "Novo Nome");
    }

    @Test
    void deveRetornar200AoListarAdministradores() throws Exception {
        List<EntityModel<AdministradorDTO>> administradores = List.of(EntityModel.of(administradorDTO));

        PagedModel<EntityModel<AdministradorDTO>> pagedModel = PagedModel.of(
            administradores,
            new PagedModel.PageMetadata(1, 0, 1)
        );

        when(administradorService.findAll(any(Pageable.class))).thenReturn(pagedModel);

        mockMvc.perform(get("/api/v1/administradores")
                .param("page", "0")
                .param("size", "10")
                .param("direction", "asc")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$._embedded.administradorDTOList[0].nome").value("Administrador Teste"))
            .andExpect(jsonPath("$._embedded.administradorDTOList[0].nomeUsuario").value("adminTeste")); 
    }

    @Test
    void deveRetornar200AoAcharAdministradorPorId() throws Exception {
        when(administradorService.findById(1L)).thenReturn(administradorDTO);

        mockMvc.perform(get("/api/v1/administradores/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Administrador Teste"))
                .andExpect(jsonPath("$.nomeUsuario").value("adminTeste"));
    }

    @Test
    void deveRetornarNotFoundQuandoNaoAcharAdministradorPorId() throws Exception {
        when(administradorService.findById(99L)).thenThrow(new EntityNotFoundException("Administrador não encontrado."));

        mockMvc.perform(get("/api/v1/administradores/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Administrador não encontrado."));
    }

    @Test
    void deveRetornar200AoAcharAdministradorPorNomeUsuario() throws Exception {
        when(administradorService.findByNomeUsuario("adminTeste")).thenReturn(administradorDTO);
        
        mockMvc.perform(get("/api/v1/administradores/nomeUsuario/{nomeUsuario}", "adminTeste")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Administrador Teste"))
                .andExpect(jsonPath("$.nomeUsuario").value("adminTeste"));
    }

    @Test
    void deveRetornarNotFoundQuandoNaoAcharAdministradorPorNomeUsuario() throws Exception {
        when(administradorService.findByNomeUsuario("naoExistente")).thenThrow(new EntityNotFoundException("Administrador não encontrado."));

        mockMvc.perform(get("/api/v1/administradores/nomeUsuario/{nomeUsuario}", "naoExistente"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Administrador não encontrado."));
    }

    @Test
    void deveRetornar200AoAtualizarAdministrador() throws Exception {
        when(administradorService.update(any(AdministradorDTO.class), eq("adminTeste"))).thenReturn(administradorUpdate);

        String json = "{"
            + "\"nome\":\"Administrador Teste Diferente\","
            + "\"nomeUsuario\":\"adminTesteDiferente\","
            + "\"email\":\"admindiferente@teste.com\","
            + "\"senha\":\"senha123\","
            + "\"celular\":\"(11) 91234-5678\","
            + "\"fotoPerfil\":\"foto.png\""
            + "}";

        mockMvc.perform(put("/api/v1/administradores/{nomeUsuario}", "adminTeste")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Administrador Teste Diferente"))
                .andExpect(jsonPath("$.nomeUsuario").value("adminTesteDiferente"))
                .andExpect(jsonPath("$.email").value("admindiferente@teste.com"));
    }

    @Test
    void deveRetornarNotFoundAoAtualizarAdministradorInexistente() throws Exception{
        String json = """
        {
          "nome": "Novo Nome",
          "nomeUsuario": "naoExistente",
          "email": "teste@teste.com",
          "senha": "123",
          "celular": "(11) 91111-1111",
          "fotoPerfil": "foto.png"
        }
        """;

        when(administradorService.update(any(AdministradorDTO.class), eq("naoExistente"))).thenThrow(new EntityNotFoundException("Administrador não encontrado."));

        mockMvc.perform(put("/api/v1/administradores/{nomeUsuario}", "naoExistente")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Administrador não encontrado."));
    }

    @Test
    void deveRetornar200AoAtualizarParcialAdministrador() throws Exception {
        when(administradorService.partialUpdate("adminTeste", updates)).thenReturn(administradorPartialUpdate);

        mockMvc.perform(patch("/api/v1/administradores/{nomeUsuario}", "adminTeste")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Novo Nome"));
    }

    @Test
    void deveRetornarNotFoundAoAtualizarParcialAdministradorInexistente() throws Exception {
        when(administradorService.partialUpdate("adminTesteErrado", updates)).thenThrow(new EntityNotFoundException("Administrador não encontrado."));

        mockMvc.perform(patch("/api/v1/administradores/{nomeUsuario}", "adminTesteErrado")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Administrador não encontrado."));
    }

    @Test
    void deveRetornarNoContentAoDeletarAdministrador() throws Exception {
        doNothing().when(administradorService).delete("adminTeste");

        mockMvc.perform(delete("/api/v1/administradores/{nomeUsuario}", "adminTeste"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveRetornarNotFoundAoDeletarAdministradorInexistente() throws Exception {
        doThrow(new EntityNotFoundException("Administrador não encontrado")).when(administradorService).delete("naoExistente");

        mockMvc.perform(delete("/api/v1/administradores/{nomeUsuario}", "naoExistente"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Administrador não encontrado"));
    }

}