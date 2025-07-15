package br.com.alevhvm.adotai.animal.dto;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;

import br.com.alevhvm.adotai.common.vo.DescricaoVO;
import br.com.alevhvm.adotai.common.vo.EnderecoVO;
import br.com.alevhvm.adotai.animal.enums.StatusAnimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder ({ "nome", "especie", "raca", "dataNascimento", "foto", "descricao", "porte", "sexo", "status", "nomeOng", "telefoneOng", "enderecoOng"})
public class AnimalDTO extends RepresentationModel<AnimalDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "idAnimal")
    @Mapping("idAnimal")
    private Long key;
    
    @NotBlank(message = "O nome do animal é obrigatório")
    @NotNull(message = "O nome do animal é obrigatório")
    private String nome;

    @NotBlank(message = "A espécie do animal é obrigatória")
    @NotNull(message = "A espécie do animal é obrigatória")
    private String especie;

    @NotBlank(message = "A raça do animal é obrigatória")
    @NotNull(message = "A raça do animal é obrigatória")
    private String raca;

    @NotNull(message = "A data de nascimento do animal é obrigatória")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;

    private String foto;

    private DescricaoVO descricao;

    @NotBlank(message = "O porte do animal é obrigatório")
    @NotNull(message = "O porte do animal é obrigatório")
    private String porte;

    @NotBlank(message = "O sexo do animal é obrigatório")
    @NotNull(message = "O sexo do animal é obrigatório")
    private String sexo;

    private StatusAnimal status;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Mapping("ong.idOng")
    private Long idOng;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Mapping("ong.nome")
    private String nomeOng;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Mapping("ong.cell")
    private String telefoneOng;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Mapping("ong.endereco")
    private EnderecoVO enderecoOng;
}