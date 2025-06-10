package br.com.alevh.sistema_adocao_pets.data.dto.v1;

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
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder({ "nome", "email", "celular" })
public class UsuarioDTO extends RepresentationModel<UsuarioDTO> implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "idUsuario")
    @Mapping("idUsuario")
    private Long key;

    private String nome;

    private String email;

    private String nomeUsuario;

    private String fotoPerfil;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    @JsonProperty(value = "celular")
    private String cell;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    //Trocar para CpfDTO
    private String cpf;
}