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
@JsonPropertyOrder ({ "nome", "email", "endereco", "telefone", "cnpj", "responsavel" })
public class OngDTO extends RepresentationModel<OngDTO> implements Serializable{
    private static final long serialVersionUID = 1L;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "idOng")
    @Mapping("idOng")
    private Long key;

    private String nome;

    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    private String endereco;

    private String telefone;

    //Trocar para CnpjDTO
    private String cnpj;

    private String responsavel;
}