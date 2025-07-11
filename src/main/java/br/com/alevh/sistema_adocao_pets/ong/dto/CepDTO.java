package br.com.alevh.sistema_adocao_pets.ong.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CepDTO {
    private String cep;
    private String logradouro;
    private String complemento;
    private String bairro;
    private String localidade; //cidade
    private String uf; //estado
}
