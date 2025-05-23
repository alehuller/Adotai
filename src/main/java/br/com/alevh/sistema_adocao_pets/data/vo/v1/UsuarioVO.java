package br.com.alevh.sistema_adocao_pets.data.vo.v1;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

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
@JsonPropertyOrder({ "nome", "email", "cell" })
public class UsuarioVO extends RepresentationModel<UsuarioVO> implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("idUsuario")
    @Mapping("idUsuario")
    private Long key;

    private String nome;

    private String email;

    private String senha;

    private String cell;

    private String cpf;
}
