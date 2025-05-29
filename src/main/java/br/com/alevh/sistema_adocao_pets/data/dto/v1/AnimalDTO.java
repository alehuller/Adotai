package br.com.alevh.sistema_adocao_pets.data.dto.v1;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@JsonPropertyOrder ({ "nome", "especie", "raca", "dataNascimento", "foto", "descricao", "porte", "sexo", "status", "nomeOng", "telefoneOng", "enderecoOng"})
public class AnimalDTO extends RepresentationModel<AnimalDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "idAnimal")
    @Mapping("idAnimal")
    private Long key;
    
    private String nome;

    private String especie;

    private String raca;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    private String foto;

    private String descricao;

    private String porte;

    private String sexo;

    private String status;

    @Mapping("ong.idOng")
    private Long idOng;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Mapping("ong.nome")
    private String nomeOng;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Mapping("ong.telefone")
    private String telefoneOng;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Mapping("ong.endereco")
    private String enderecoOng;
}