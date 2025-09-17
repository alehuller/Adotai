package br.com.alevhvm.adotai.auth.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alevhvm.adotai.administrador.dto.AdministradorDTO;
import br.com.alevhvm.adotai.administrador.service.AdministradorService;
import br.com.alevhvm.adotai.auth.dto.LoginDTO;
import br.com.alevhvm.adotai.auth.dto.RegistroDTO;
import br.com.alevhvm.adotai.auth.dto.TokenDTO;
import br.com.alevhvm.adotai.auth.security.SecurityFilter;
import br.com.alevhvm.adotai.auth.service.TokenBlackListService;
import br.com.alevhvm.adotai.auth.service.TokenService;
import br.com.alevhvm.adotai.common.util.MediaType;
import br.com.alevhvm.adotai.common.vo.CnpjVO;
import br.com.alevhvm.adotai.common.vo.CpfVO;
import br.com.alevhvm.adotai.common.vo.EnderecoVO;
import br.com.alevhvm.adotai.common.vo.RedeVO;
import br.com.alevhvm.adotai.ong.dto.OngDTO;
import br.com.alevhvm.adotai.ong.service.OngService;
import br.com.alevhvm.adotai.usuario.dto.UsuarioDTO;
import br.com.alevhvm.adotai.usuario.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;

@WebMvcTest(
    controllers = AuthController.class,
    excludeFilters = @org.springframework.context.annotation.ComponentScan.Filter(
        type = FilterType.ASSIGNABLE_TYPE,
        classes = SecurityFilter.class
    )
)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AdministradorService administradorService;

    @MockitoBean
    private UsuarioService usuarioService;

    @MockitoBean
    private OngService ongService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private TokenService tokenService;

    @MockitoBean
    private TokenBlackListService tokenBlackListService;

    private LoginDTO loginDTO;
    private TokenDTO tokenDTO;
    private RegistroDTO registroDTO;
    private UsuarioDTO usuarioDTO;
    private OngDTO ongDTO;
    private AdministradorDTO administradorDTO;

    private RedeVO redeVO;
    private CpfVO cpfVO;

    @BeforeEach
    void setUp() {
        loginDTO = new LoginDTO("teste@email.com", "senha123");
        tokenDTO = new TokenDTO("fake-jwt-token");

        redeVO = new RedeVO();
        redeVO.setInstagram("https://teste.adotai.com/");
        redeVO.setFacebook("https://teste.facebook.com/adotai");

        cpfVO = new CpfVO("181.018.320-09");

        registroDTO = new RegistroDTO();
        registroDTO.setNome("Nome Teste");
        registroDTO.setNomeUsuario("NomeUsuario");
        registroDTO.setEmail("email@teste.com");
        registroDTO.setPassword("senha123");
        registroDTO.setCell("(11) 98888-8888");
        registroDTO.setCpf(cpfVO);
        registroDTO.setFotoPerfil("Foto Teste");

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setKey(1L);
        usuarioDTO.setNome("Nome Teste");
        usuarioDTO.setNomeUsuario("NomeUsuario");
        usuarioDTO.setEmail("email@teste.com");
        usuarioDTO.setCell("(11) 98888-8888");
        usuarioDTO.setCpf(cpfVO);
        usuarioDTO.setFotoPerfil("Foto Perfil");

        ongDTO = new OngDTO();
        ongDTO.setKey(1L);
        ongDTO.setNome("Amigos dos Animais");
        ongDTO.setNomeUsuario("amigosanimais");
        ongDTO.setFotoPerfil("foto_ong.png");
        ongDTO.setEmail("contato@amigosanimais.org");
        ongDTO.setSenha("123456");
        ongDTO.setEndereco(new EnderecoVO(
                "Rua das Flores",
                "123",
                "Casa 2",
                "Centro",
                "São Paulo",
                "SP",
                "01000-000"
        ));
        ongDTO.setCell("(11) 98888-7777");
        ongDTO.setCnpj(new CnpjVO("05.695.557/0001-08"));
        ongDTO.setResponsavel("Maria Silva");
        ongDTO.setDescricao("ONG dedicada ao resgate e adoção de animais abandonados.");
        ongDTO.setRede(redeVO);

        administradorDTO = new AdministradorDTO();
        administradorDTO.setKey(1L);
        administradorDTO.setNome("AdmTeste");
        administradorDTO.setNomeUsuario("AdmTeste");
        administradorDTO.setEmail("emailteste@teste.com");
        administradorDTO.setSenha("senha123");
        administradorDTO.setCell("(11) 92222-2222");
        administradorDTO.setFotoPerfil("teste foto");
    }

    @Test
    void deveRetornar200AoLogarUsuario() throws Exception{
        when(usuarioService.logar(any(LoginDTO.class))).thenReturn(tokenDTO);

        mockMvc.perform(post("/auth/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("fake-jwt-token"));
    }

    @Test
    void deveRetornar201AoRegistrarUsuario() throws Exception {
        when(usuarioService.create(any(RegistroDTO.class))).thenReturn(usuarioDTO);

        String json = """
        {
            "email": "email@teste.com",
            "senha": "senha123",
            "nome": "Nome Teste",
            "nomeUsuario": "NomeUsuario",
            "fotoPerfil": "Foto Teste",
            "celular": "(11) 98888-8888",
            "cpf": "181.018.320-09"
            }
        }
        """;

        mockMvc.perform(post("/auth/user/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/api/v1/usuarios/id/1"))
        .andExpect(jsonPath("$.nome").value("Nome Teste"))
        .andExpect(jsonPath("$.email").value("email@teste.com"))
        .andExpect(jsonPath("$.celular").value("(11) 98888-8888"));
    }

    @Test
    void deveRetornarBadRequestQuandoRegistroDeUsuarioForInvalido() throws Exception {
        String json = """
        {
            "email": "",
            "senha": "123",
            "nome": "",
            "nomeUsuario": "nome usuario", 
            "fotoPerfil": "",
            "celular": "11999999999"
        }
        """;

        mockMvc.perform(post("/auth/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());       
    }

    @Test
    void deveRetornar200AoLogarOng() throws Exception {
        when(ongService.logar(any(LoginDTO.class))).thenReturn(tokenDTO);

        mockMvc.perform(post("/auth/ong/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("fake-jwt-token"));
    }

    @Test
    void deveRetornar201AoRegistrarOng() throws Exception {
        when(ongService.create(any(OngDTO.class))).thenReturn(ongDTO);

        String json = "{"
            + "\"nome\":\"Amigos dos Animais\","
            + "\"nomeUsuario\":\"amigosanimais\","
            + "\"fotoPerfil\":\"foto_ong.png\","
            + "\"email\":\"contato@amigosanimais.org\","
            + "\"senha\":\"123456\","
            + "\"endereco\":{"
                + "\"logradouro\":\"Rua das Flores\","
                + "\"numero\":\"123\","
                + "\"complemento\":\"Casa 2\","
                + "\"bairro\":\"Centro\","
                + "\"cidade\":\"São Paulo\","
                + "\"estado\":\"SP\","
                + "\"cep\":\"01000-000\""
            + "},"
            + "\"cell\":\"(11) 98888-7777\","
            + "\"cnpj\":\"05.695.557/0001-08\","
            + "\"responsavel\":\"Maria Silva\","
            + "\"descricao\":\"ONG dedicada ao resgate e adoção de animais abandonados.\","
            + "\"rede\":{"
                + "\"instagram\":\"https://teste.adotai.com/\","
                + "\"facebook\":\"https://teste.facebook.com/adotai\""
            + "}"
        + "}"; 


        mockMvc.perform(post("/auth/ong/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/v1/ongs/id/1"))
                .andExpect(jsonPath("$.nome").value("Amigos dos Animais"))
                .andExpect(jsonPath("$.email").value("contato@amigosanimais.org"))
                .andExpect(jsonPath("$.descricao").value("ONG dedicada ao resgate e adoção de animais abandonados."));
    }

    @Test
    void deveRetonarBadRequestQuandoRegistroDeOngForInvalido() throws Exception {
        String json = """
        {
            "nome": "",
            "nomeUsuario": "ong teste",
            "fotoPerfil": "",
            "email": "email-invalido",
            "senha": "123",
            "cell": "11999999999",
            "responsavel": "",
            "descricao": ""
        }
        """;

        mockMvc.perform(post("/auth/ong/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar200AoLogarAdministrador() throws Exception {
        when(administradorService.logar(any(LoginDTO.class))).thenReturn(tokenDTO);

        mockMvc.perform(post("/auth/admin/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("fake-jwt-token"));   
    }

    @Test
    void deveRetornar201AoRegistrarAdministrador() throws Exception {
        when(administradorService.create(any(AdministradorDTO.class))).thenReturn(administradorDTO);

        String json = "{"
            + "\"nome\":\"AdmTeste\","
            + "\"nomeUsuario\":\"AdmTeste\","
            + "\"email\":\"emailteste@teste.com\","
            + "\"senha\":\"senha123\","
            + "\"celular\":\"(11) 92222-2222\","
            + "\"fotoPerfil\":\"teste foto\""
        + "}"; 

        mockMvc.perform(post("/auth/admin/register")
        .contentType(MediaType.APPLICATION_JSON)
        .content(json))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/api/v1/administradores/id/1"))
        .andExpect(jsonPath("$.nome").value("AdmTeste"))
        .andExpect(jsonPath("$.email").value("emailteste@teste.com"))
        .andExpect(jsonPath("$.fotoPerfil").value("teste foto"));
    }

    @Test
    void deveRetornarBadRequestQuandoRegistroDeAdministradorForInvalido() throws Exception {
        String json = """
        {
            "nome": "",
            "nomeUsuario": "adm teste",
            "email": "email-invalido",
            "senha": "",
            "celular": "11988887777",
            "fotoPerfil": "foto.png"
        }
        """;

        mockMvc.perform(post("/auth/admin/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar200AoFazerSignout() throws Exception {
        doNothing().when(tokenBlackListService).addToBlacklist(any(HttpServletRequest.class));

        mockMvc.perform(post("/auth/signout")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Logout realizado com sucesso"));   
    }
}
