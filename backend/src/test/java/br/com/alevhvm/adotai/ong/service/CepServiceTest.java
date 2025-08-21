package br.com.alevhvm.adotai.ong.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import br.com.alevhvm.adotai.ong.dto.CepDTO;

@ExtendWith(MockitoExtension.class)
public class CepServiceTest {

    @InjectMocks
    private CepService cepService;

    @Mock
    private RestTemplate restTemplate;

    String cep;
    CepDTO cepDTO;
    String url;

    ResponseEntity<CepDTO> responseEntity;

    @BeforeEach
    void setUp() {
        cep = "03034-000";
        url = "https://viacep.com.br/ws/" + cep.replaceAll("[^\\d]", "") + "/json/";

        cepDTO = new CepDTO();
        cepDTO.setCep(cep);
        cepDTO.setLogradouro("Rua Araguaia");
        cepDTO.setBairro("Canindé");
        cepDTO.setLocalidade("São Paulo");
        cepDTO.setUf("SP");
    }

    @Test
    void deveRetornarOEnderecoDaBuscaPorCep() {
        responseEntity = new ResponseEntity<>(cepDTO, HttpStatus.OK);
        when(restTemplate.getForEntity(url, CepDTO.class)).thenReturn(responseEntity);

        CepDTO resposta = cepService.buscarEnderecoPorCep(cep);

        assertNotNull(resposta);
        assertEquals("03034-000", resposta.getCep());
        assertEquals("Rua Araguaia", resposta.getLogradouro());
        assertEquals("Canindé", resposta.getBairro());
        assertEquals("São Paulo", resposta.getLocalidade());
    }

    @Test
    void deveLancarExcecaoQuandoCepEstiverInvalido() {
        responseEntity = new ResponseEntity<>(null, HttpStatus.OK);
        when(restTemplate.getForEntity(url, CepDTO.class)).thenReturn(responseEntity);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            cepService.buscarEnderecoPorCep(cep);
        });

        assertEquals("CEP inválido ou não encontrado", ex.getMessage());
    }
    
    @Test
    void deveLancarExcecaoQuandoStatusNaoEstiverOk() {
        responseEntity = new ResponseEntity<>(new CepDTO(), HttpStatus.BAD_REQUEST);
        when(restTemplate.getForEntity(url, CepDTO.class)).thenReturn(responseEntity);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> {
            cepService.buscarEnderecoPorCep(cep);
        });

        assertEquals("CEP inválido ou não encontrado", ex.getMessage());
    }
}
