package br.com.alevhvm.adotai.common.docs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class DocsControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    private String obterJsonDocumentacao() throws Exception {
        return mockMvc.perform(get("/v3/api-docs"))
            .andReturn()
            .getResponse()
            .getContentAsString();
    }

    @Test
    void deveConterEndpointsDeAdministradorNaDocumentacao() throws Exception {
        String json = obterJsonDocumentacao();

        assertThat(json).contains("/api/v1/administradores");
        assertThat(json).contains("/api/v1/administradores/{id}");
        assertThat(json).contains("/api/v1/administradores/nomeUsuario/{nomeUsuario}");
        assertThat(json).contains("/api/v1/administradores/{nomeUsuario}");
    }

    @Test
    void deveConterEndpointsDeAdocaoNaDocumentacao() throws Exception {
        String json = obterJsonDocumentacao();

        assertThat(json).contains("/api/v1/adocoes");
        assertThat(json).contains("/api/v1/adocoes/{id}");
    }

    @Test
    void deveConterEndpointsDeAnimalNaDocumentacao() throws Exception {
        String json = obterJsonDocumentacao();

        assertThat(json).contains("/api/v1/animais");
        assertThat(json).contains("/api/v1/animais/{id}");
        assertThat(json).contains("/api/v1/animais/nome/{nome}");
        assertThat(json).contains("/api/v1/animais/filtro");
        assertThat(json).contains("/api/v1/animais/{nome}");
    }

    @Test
    void deveConterEndpointsDeAuthNaDocumentacao() throws Exception {
        String json = obterJsonDocumentacao();

        assertThat(json).contains("/auth");
        assertThat(json).contains("/auth/user/login");
        assertThat(json).contains("/auth/user/register");
        assertThat(json).contains("/auth/ong/login");
        assertThat(json).contains("/auth/ong/register");
        assertThat(json).contains("/auth/admin/login");
        assertThat(json).contains("/auth/admin/register");
        assertThat(json).contains("/auth/signout");
    }

    @Test
    void deveConterEndpointsDeOngNaDocumentacao() throws Exception {
        String json = obterJsonDocumentacao();

        assertThat(json).contains("/api/v1/ongs");
        assertThat(json).contains("/api/v1/ongs/{id}");
        assertThat(json).contains("/api/v1/ongs/nomeUsuario/{nomeUsuario}");
        assertThat(json).contains("/api/v1/ongs/{id}/adocoes");
        assertThat(json).contains("/api/v1/ongs/{nomeUsuario}/animais");
        assertThat(json).contains("/api/v1/ongs/{nomeUsuario}");
        assertThat(json).contains("/api/v1/ongs/filtro");
    }

    @Test
    void deveConterEndpointsDeUsuarioNaDocumentacao() throws Exception {
        String json = obterJsonDocumentacao();

        assertThat(json).contains("/api/v1/usuarios");
        assertThat(json).contains("/api/v1/usuarios/{id}");
        assertThat(json).contains("/api/v1/usuarios/nomeUsuario/{nomeUsuario}");
        assertThat(json).contains("/api/v1/usuarios/{nomeUsuario}/adocoes");
        assertThat(json).contains("/api/v1/usuarios/{nomeUsuario}/favoritos");
        assertThat(json).contains("/api/v1/usuarios/favoritar/{nomeUsuario}/{animalId}");
        assertThat(json).contains("/api/v1/usuarios/{nomeUsuario}");
    }

}
