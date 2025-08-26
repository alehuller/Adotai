package br.com.alevhvm.adotai.administrador.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

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

    @BeforeEach
    void setUp() {
        administradorDTO = new AdministradorDTO();
        administradorDTO.setNome("Administrador Teste");
        administradorDTO.setNomeUsuario("adminTeste");
        administradorDTO.setEmail("admin@teste.com");
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
}
