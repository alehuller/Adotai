package br.com.alevhvm.adotai.ong.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.alevhvm.adotai.common.vo.EnderecoVO;
import br.com.alevhvm.adotai.ong.dto.CepDTO;
import br.com.alevhvm.adotai.ong.exception.CepException;

@Service
public class CepService {

    private final RestTemplate restTemplate;

    public CepService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CepDTO buscarEnderecoPorCep(String cep) {
        String url = "https://viacep.com.br/ws/" + cep.replaceAll("[^\\d]", "") + "/json/";
        ResponseEntity<CepDTO> response = restTemplate.getForEntity(url, CepDTO.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null
                && response.getBody().getCep() != null) {
            return response.getBody();
        } else {
            throw new CepException("CEP inválido ou não encontrado");
        }
    }

    public void preencherEndereco(EnderecoVO enderecoVO) {
        if (enderecoVO == null || enderecoVO.getCep() == null)
            return;

        CepDTO resposta = buscarEnderecoPorCep(enderecoVO.getCep());

        enderecoVO.setLogradouro(resposta.getLogradouro());
        enderecoVO.setBairro(resposta.getBairro());
        enderecoVO.setCidade(resposta.getLocalidade());
        enderecoVO.setEstado(resposta.getUf());
    }
}
