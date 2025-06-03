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
@JsonPropertyOrder({})
public class AdocaoDTO extends RepresentationModel<AdocaoDTO> implements Serializable{
    private static final long serialVersionUID = 1L;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "idAdocao")
    @Mapping("idAdocao")
    private Long key;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataAdocao;

    private String status;

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

    @Mapping("usuario.idUsuario")
    private Long idUsuario;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Mapping("usuario.nome")
    private String nomeUsuario;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Mapping("usuario.email")
    private String emailusuario;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Mapping("usuario.cell")
    private String cellUsuario;
}
