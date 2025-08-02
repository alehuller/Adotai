package br.com.alevhvm.adotai.commom.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import br.com.alevhvm.adotai.common.util.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.hamcrest.Matchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;



@SpringBootTest
@AutoConfigureMockMvc
public class WebConfigTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Test
    void devePermitirCorsParaDominiosConfigurados() throws Exception {
        mockMvc.perform(options("/api/v1/animais")
                .header("Origin", "http://localhost:3000")
                .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Origin", "http://localhost:3000"))
                .andExpect(header().string("Access-Control-Allow-Methods", Matchers.containsString("GET")));
    }

    @Test
    void naoDevePermitirCorsNaoPermitidoParaAcessarDominioNaoConfigurado() throws Exception {
        mockMvc.perform(options("/api/v1/animais")
                .header("Origin", "http://www.teste.com")
                .header("Access-Control-Request-Method", "GET"))
                .andExpect(status().isForbidden())
                .andExpect(header().doesNotExist("Access-Control-Allow-Origin"));
    }

    @Test
    void deveRetornarJsonQuandoAcceptForJson() throws Exception {
        mockMvc.perform(get("/api/v1/animais")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void deveRetornarXmlQuandoAcceptForXml() throws Exception {
        mockMvc.perform(get("/api/v1/animais")
                .accept(MediaType.APPLICATION_XML))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_XML));
    }

    @Test
    void deveRetornarYmlQuandoAcceptForYml() throws Exception {
        mockMvc.perform(get("/api/v1/animais")
                .accept("application/x-yaml"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/x-yaml"));
    }
}
