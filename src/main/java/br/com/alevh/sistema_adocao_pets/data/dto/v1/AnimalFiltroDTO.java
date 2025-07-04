package br.com.alevh.sistema_adocao_pets.data.dto.v1;

import br.com.alevh.sistema_adocao_pets.enums.StatusAnimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimalFiltroDTO {
    private String especie;
    private String raca;
    private String porte;
    private String sexo;
    private StatusAnimal status;
    private String cidadeOng;
}
