package br.com.alevhvm.adotai.adocao.dto;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;

import br.com.alevhvm.adotai.adocao.enums.StatusAdocao;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder({ "dataAdocao", "status", "nomeAnimal", "especieAnimal", "sexoAnimal", "nomeUsuario", "emailUsuario",
        "celularUsuario" })
public class AdocaoDTO extends RepresentationModel<AdocaoDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "idAdocao")
    @Mapping("idAdocao")
    private Long key;

    @NotNull(message = "A data de adoção é obrigatória")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataAdocao;

    @NotNull(message = "O status da adoção é obrigatório")
    private StatusAdocao status;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Mapping("animal.idAnimal")
    private Long idAnimal;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Mapping("animal.nome")
    private String nomeAnimal;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Mapping("animal.especie")
    private String especieAnimal;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Mapping("animal.sexo")
    private String sexoAnimal;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Mapping("usuario.idUsuario")
    private Long idUsuario;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Mapping("usuario.nome")
    private String nomeUsuario;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Mapping("usuario.email")
    private String emailUsuario;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "celularUsuario")
    @Mapping("usuario.cell")
    private String cellUsuario;
}