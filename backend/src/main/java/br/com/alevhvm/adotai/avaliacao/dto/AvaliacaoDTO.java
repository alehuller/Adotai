package br.com.alevhvm.adotai.avaliacao.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.github.dozermapper.core.Mapping;

import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AvaliacaoDTO extends RepresentationModel<AvaliacaoDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY, value = "id")
    @Mapping("id")
    private Long key;

    @NotNull(message = "O id do usuario nao pode ser nulo")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Mapping("usuario.idUsuario")
    private Long idUsuario;

    @NotNull(message = "O id da ong nao pode ser nulo")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Mapping("ong.idOng")
    private Long idOng;
    
    @NotNull(message = "A nota da avaliacao nao pode ser nula")
    private Integer nota;

    private String comentario;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime dataCriacao;
}
