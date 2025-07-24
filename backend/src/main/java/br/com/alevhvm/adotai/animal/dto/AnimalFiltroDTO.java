package br.com.alevhvm.adotai.animal.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnimalFiltroDTO {
    private String nome;
    private String especie;
    private String raca;
    private String porte;
    private String sexo;
    private String status;
    private String cidadeOng;
}
