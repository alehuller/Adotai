package br.com.alevh.sistema_adocao_pets.data.vo.v1;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@JsonPropertyOrder ({ "nome, email, endereco, telefone, cnpj, responsavel" })
public class OngVO extends RepresentationModel<OngVO> implements Serializable{
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @JsonProperty("idOng")
    @Mapping("idOng")
    private Long key;

    private String nome;

    private String email;

    @JsonIgnore
    private String senha;

    private String endereco;

    private String telefone;

    private String cnpj;

    private String responsavel;
}
