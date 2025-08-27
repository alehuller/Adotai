package br.com.alevhvm.adotai.administrador.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @MockitoBean
    private AdministradorService administradorService;

    private AdministradorDTO administradorDTO;
    private AdministradorDTO administradorUpdate;
    private AdministradorDTO administradorPartialUpdate;

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
    }

    @Test
    void deveListarAdministradores() throws Exception {
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
    void deveAcharAdministradorPorId() throws Exception {
        when(administradorService.findById(1L)).thenReturn(administradorDTO);

        mockMvc.perform(get("/api/v1/administradores/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Administrador Teste"))
                .andExpect(jsonPath("$.nomeUsuario").value("adminTeste"));
    }

    @Test
    void deveAcharAdministradorPorNomeUsuario() throws Exception {
        when(administradorService.findByNomeUsuario("adminTeste")).thenReturn(administradorDTO);
        
        mockMvc.perform(get("/api/v1/administradores/nomeUsuario/{nomeUsuario}", "adminTeste")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Administrador Teste"))
                .andExpect(jsonPath("$.nomeUsuario").value("adminTeste"));
    }

    @Test
    void deveAtualizarAdministrador() throws Exception {
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
    void deveAtualizarParcialAdministrador() throws Exception {
        Map<String, Object> updates = Map.of("nome", "Novo Nome");
        when(administradorService.partialUpdate("adminTeste", updates)).thenReturn(administradorPartialUpdate);

        mockMvc.perform(patch("/api/v1/administradores/{nomeUsuario}", "adminTeste")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Novo Nome"));
    }

    @Test
    void deveDeletarAdministrador() throws Exception {
        doNothing().when(administradorService).delete("adminTeste");

        mockMvc.perform(delete("/api/v1/administradores/{nomeUsuario}", "adminTeste"))
                .andExpect(status().isNoContent());
    }
}