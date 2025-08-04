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

    @Test
    void deveConterEndpointDeAdministradorNaDocumentacao() throws Exception {
        String openApiJson = mockMvc.perform(get("/v3/api-docs"))
            .andReturn()
            .getResponse()
            .getContentAsString();

        assertThat(openApiJson).contains("/api/v1/administradores");
    }

    @Test
    void deveConterEndpointDeAdocaoNaDocumentacao() throws Exception {
        String openApiJson = mockMvc.perform(get("/v3/api-docs"))
            .andReturn()
            .getResponse()
            .getContentAsString();

        assertThat(openApiJson).contains("/api/v1/adocoes");
    }

    @Test
    void deveConterEndpointDeAnimalNaDocumentacao() throws Exception {
        String openApiJson = mockMvc.perform(get("/v3/api-docs"))
            .andReturn()
            .getResponse()
            .getContentAsString();

        assertThat(openApiJson).contains("/api/v1/animais");
    }

    @Test
    void deveConterEndpointDeAuthNaDocumentacao() throws Exception {
        String openApiJson = mockMvc.perform(get("/v3/api-docs"))
            .andReturn()
            .getResponse()
            .getContentAsString();

        assertThat(openApiJson).contains("/auth");
    }

    @Test
    void deveConterEndpointDeOngNaDocumentacao() throws Exception {
        String openApiJson = mockMvc.perform(get("/v3/api-docs"))
            .andReturn()
            .getResponse()
            .getContentAsString();

        assertThat(openApiJson).contains("/api/v1/ongs");
    }

    @Test
    void deveConterEndpointDeUsuarioNaDocumentacao() throws Exception {
        String openApiJson = mockMvc.perform(get("/v3/api-docs"))
            .andReturn()
            .getResponse()
            .getContentAsString();

        assertThat(openApiJson).contains("/api/v1/usuarios");
    }

}
