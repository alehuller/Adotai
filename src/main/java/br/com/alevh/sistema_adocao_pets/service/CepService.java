package br.com.alevh.sistema_adocao_pets.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.alevh.sistema_adocao_pets.data.dto.common.EnderecoVO;
import br.com.alevh.sistema_adocao_pets.integration.ViaCepResponse;

@Service
public class CepService {
    
    private final RestTemplate restTemplate;

    public CepService() {
        this.restTemplate = new RestTemplate();
    }

    public ViaCepResponse buscarEnderecoPorCep(String cep) {
        String url = "https://viacep.com.br/ws/" + cep.replaceAll("[^\\d]", "") + "/json/";
        ResponseEntity<ViaCepResponse> response = restTemplate.getForEntity(url, ViaCepResponse.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null && response.getBody().getCep() != null) {
            return response.getBody();
        } else {
            throw new IllegalArgumentException("CEP inválido ou não encontrado");
        }
    }

    public void preencherEndereco(EnderecoVO enderecoVO) {
        if(enderecoVO == null || enderecoVO.getCep() == null) return;

        ViaCepResponse resposta = buscarEnderecoPorCep(enderecoVO.getCep());

        enderecoVO.setLogradouro(resposta.getLogradouro());
        enderecoVO.setBairro(resposta.getBairro());
        enderecoVO.setCidade(resposta.getLocalidade());
        enderecoVO.setEstado(resposta.getUf());
    }
}
