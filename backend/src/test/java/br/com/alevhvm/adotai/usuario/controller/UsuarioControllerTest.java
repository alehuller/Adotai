package br.com.alevhvm.adotai.usuario.controller;

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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


import java.time.LocalDate;
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
import br.com.alevhvm.adotai.animal.dto.AnimalDTO;
import br.com.alevhvm.adotai.animal.enums.StatusAnimal;
import br.com.alevhvm.adotai.auth.dto.RegistroDTO;
import br.com.alevhvm.adotai.auth.security.SecurityFilter;
import br.com.alevhvm.adotai.common.util.MediaType;
import br.com.alevhvm.adotai.common.vo.CpfVO;
import br.com.alevhvm.adotai.common.vo.DescricaoVO;
import br.com.alevhvm.adotai.usuario.dto.UsuarioDTO;
import br.com.alevhvm.adotai.usuario.dto.UsuarioUpdateDTO;
import br.com.alevhvm.adotai.usuario.service.UsuarioService;

@WebMvcTest(
    controllers = UsuarioController.class,
    excludeFilters = @org.springframework.context.annotation.ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = SecurityFilter.class
    )
)
@AutoConfigureMockMvc(addFilters = false)
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioService usuarioService;

    private UsuarioDTO usuarioDTO;
    private UsuarioDTO usuarioUpdateDTO;
    private UsuarioDTO usuarioPartialUpdate;
    private AdocaoDTO adocaoDTO;
    private RegistroDTO registroDTO;
    private AnimalDTO animalDTO;
    private DescricaoVO descricaoVO;

    @BeforeEach
    void setUp() {
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setKey(1L);
        usuarioDTO.setNome("Nome Teste");
        usuarioDTO.setEmail("email@teste.com");
        usuarioDTO.setNomeUsuario("NomeUsuarioTeste");
        usuarioDTO.setSenha("senha123");
        usuarioDTO.setFotoPerfil("Foto Teste");
        usuarioDTO.setCell("(11) 91111-1111");
        usuarioDTO.setCpf(new CpfVO("104.521.970-31"));

        usuarioUpdateDTO = new UsuarioDTO();
        usuarioUpdateDTO.setNome("Nome Teste Update");
        usuarioUpdateDTO.setEmail("email@testeupdate.com");
        usuarioUpdateDTO.setNomeUsuario("NomeUsuarioTesteUpdate");
        usuarioUpdateDTO.setFotoPerfil("Foto Teste Update");
        usuarioUpdateDTO.setSenha("senha123");
        usuarioUpdateDTO.setCell("(11) 92222-2222");

        usuarioPartialUpdate = new UsuarioDTO();
        usuarioPartialUpdate.setNome("Nome Teste Partial");

        adocaoDTO = new AdocaoDTO();
        adocaoDTO.setKey(1L);
        adocaoDTO.setDataAdocao(LocalDate.parse("2024-07-11"));
        adocaoDTO.setStatus(StatusAdocao.APROVADA);

        registroDTO = new RegistroDTO();
        registroDTO.setEmail("email@teste.com");
        registroDTO.setPassword("senha123");
        registroDTO.setNome("Nome Teste");
        registroDTO.setNomeUsuario("NomeUsuarioTeste");
        registroDTO.setFotoPerfil("Foto Teste");
        registroDTO.setCell("(11) 91111-1111");
        registroDTO.setCpf(new CpfVO("104.521.970-31"));

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
    }

    @Test
    void deveListarUsuarios() throws Exception {
        List<EntityModel<UsuarioDTO>> usuarios = List.of(EntityModel.of(usuarioDTO));

        PagedModel<EntityModel<UsuarioDTO>> pagedModel = PagedModel.of(
            usuarios,
            new PagedModel.PageMetadata(1, 0, 1)
        );

        when(usuarioService.findAll(any(Pageable.class))).thenReturn(pagedModel);

        mockMvc.perform(get("/api/v1/usuarios")
                .param("page", "0")
                .param("size", "10")
                .param("direction", "asc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.usuarioDTOList[0].nome").value("Nome Teste"))
                .andExpect(jsonPath("$._embedded.usuarioDTOList[0].email").value("email@teste.com"))
                .andExpect(jsonPath("$._embedded.usuarioDTOList[0].celular").value("(11) 91111-1111"));
    }

    @Test
    void deveAcharUsuarioPorId() throws Exception {
        when(usuarioService.findById(1L)).thenReturn(usuarioDTO);

        mockMvc.perform(get("/api/v1/usuarios/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Teste"))
                .andExpect(jsonPath("$.email").value("email@teste.com"))
                .andExpect(jsonPath("$.celular").value("(11) 91111-1111"));
    }

    @Test
    void deveAcharUsuarioPorNomeUsuario() throws Exception {
        when(usuarioService.findByNomeUsuario("Nome Teste")).thenReturn(usuarioDTO);

        mockMvc.perform(get("/api/v1/usuarios/nomeUsuario/{nomeUsuario}", "Nome Teste")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Teste"))
                .andExpect(jsonPath("$.email").value("email@teste.com"))
                .andExpect(jsonPath("$.celular").value("(11) 91111-1111"));
    }

    @Test
    void deveListarAdocoesPorNomeUsuario() throws Exception{
        List<EntityModel<AdocaoDTO>> adocoes = List.of(EntityModel.of(adocaoDTO));

        PagedModel<EntityModel<AdocaoDTO>> pagedModel = PagedModel.of(
            adocoes,
            new PagedModel.PageMetadata(1, 0, 1)
        );

        when(usuarioService.findAllAdocoesByNomeUsuario(eq("Nome Teste"), any(Pageable.class))).thenReturn(pagedModel);

        mockMvc.perform(get("/api/v1/usuarios/{nomeUsuario}/adocoes", "Nome Teste")
                .param("page", "0")
                .param("size", "10")
                .param("direction", "asc")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.adocaoDTOList[0].dataAdocao").value("11/07/2024"))
                .andExpect(jsonPath("$._embedded.adocaoDTOList[0].status").value("APROVADA"));
    }

    @Test
    void deveRegistrarUsuario() throws Exception {
        when(usuarioService.create(any(RegistroDTO.class))).thenReturn(usuarioDTO);

        String json = "{"
        + "\"nome\":\"Nome Teste\","
        + "\"nomeUsuario\":\"NomeUsuarioTeste\","
        + "\"fotoPerfil\":\"Foto Teste\","
        + "\"email\":\"email@teste.com\","
        + "\"senha\":\"senha123\","
        + "\"celular\":\"(11) 91111-1111\","
        + "\"cpf\":\"990.953.970-50\""
        + "}";


        mockMvc.perform(post("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.nome").value("Nome Teste"))
                .andExpect(jsonPath("$.email").value("email@teste.com"))
                .andExpect(jsonPath("$.celular").value("(11) 91111-1111"));
    }

    @Test
    void deveAtualizarUsuario() throws Exception {
        when(usuarioService.update(any(UsuarioUpdateDTO.class), eq("Nome Teste"))).thenReturn(usuarioUpdateDTO);

        String json = "{"
        + "\"nome\":\"Nome Teste Update\","
        + "\"nomeUsuario\":\"NomeUsuarioTesteUpdate\","
        + "\"fotoPerfil\":\"Foto Teste Update\","
        + "\"email\":\"email@testeupdate.com\","
        + "\"senha\":\"senha123\","
        + "\"celular\":\"(11) 92222-2222\""
        + "}";



        mockMvc.perform(put("/api/v1/usuarios/{nomeUsuario}", "Nome Teste")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Teste Update"))
                .andExpect(jsonPath("$.email").value("email@testeupdate.com"))
                .andExpect(jsonPath("$.celular").value("(11) 92222-2222"));
    }

    @Test
    void deveAtualizarParcialUsuario() throws Exception {
        Map<String, Object> updates = Map.of("nome", "Nome Teste Partial");
        when(usuarioService.partialUpdate("Nome Teste", updates)).thenReturn(usuarioPartialUpdate);

        mockMvc.perform(patch("/api/v1/usuarios/{nomeUsuario}", "Nome Teste")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Nome Teste Partial"));
    }

    @Test
    void deveDeletarUsuario() throws Exception {
        doNothing().when(usuarioService).delete("Nome Teste");

        mockMvc.perform(delete("/api/v1/usuarios/{nomeUsuario}", "Nome Teste"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deveFavoritarAnimal() throws Exception {
        when(usuarioService.toggleAnimalFavorito("NomeUsuarioTeste", 1L)).thenReturn(true);

        mockMvc.perform(post("/api/v1/usuarios/favoritar/{nomeUsuario}/{animalId}", "NomeUsuarioTeste", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Animal favoritado com sucesso"))
                .andExpect(jsonPath("$.favorito").value(true))
                .andExpect(jsonPath("$.animalId").value(1));
    }

    @Test
    void deveDesfavoritarAnimal() throws Exception {
        when(usuarioService.toggleAnimalFavorito("NomeUsuarioTeste", 1L)).thenReturn(false);

        mockMvc.perform(post("/api/v1/usuarios/favoritar/{nomeUsuario}/{animalId}", "NomeUsuarioTeste", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensagem").value("Animal removido dos favoritos"))
                .andExpect(jsonPath("$.favorito").value(false))
                .andExpect(jsonPath("$.animalId").value(1));
    }

    @Test
    void deveListarAnimaisFavoritos() throws Exception {
        List<EntityModel<AnimalDTO>> animais = List.of(EntityModel.of(animalDTO));

        PagedModel<EntityModel<AnimalDTO>> pagedModel = PagedModel.of(
            animais,
            new PagedModel.PageMetadata(1, 0, 1)
        );

        when(usuarioService.findAnimaisFavoritosByNomeUsuario(eq("Nome Teste"), any(Pageable.class))).thenReturn(pagedModel);

        mockMvc.perform(get("/api/v1/usuarios/{nomeUsuario}/favoritos", "Nome Teste")
                .param("page", "0")
                .param("size", "10")
                .param("direction", "asc")
                .param("sort", "nome")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.animalDTOList[0].nome").value("Animal Nome"))
                .andExpect(jsonPath("$._embedded.animalDTOList[0].especie").value("Cachorro"))
                .andExpect(jsonPath("$._embedded.animalDTOList[0].raca").value("Spitz Alemão"))
                .andExpect(jsonPath("$._embedded.animalDTOList[0].status").value("ADOTADO"));
    }
} 
