package br.com.alevh.sistema_adocao_pets.data.dto.v1;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;

import br.com.alevh.sistema_adocao_pets.data.dto.common.CnpjVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder ({ "nome", "nomeUsuario", "email", "endereco", "telefone", "cnpj", "responsavel", "fotoPerfil" })
public class OngDTO extends RepresentationModel<OngDTO> implements Serializable{
    private static final long serialVersionUID = 1L;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "idOng")
    @Mapping("idOng")
    private Long key;

    private String nome;

    @Pattern(regexp = "^\\S+$", message = "O nome de usuário não pode conter espaços")
    private String nomeUsuario;

    private String fotoPerfil;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido. Insira um endereço de e-mail válido")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    private String endereco;

    @JsonProperty(value = "celular")
    @Pattern(regexp = "^\\(\\d{2}\\) \\d{5}-\\d{4}$", message = "O celular deve estar no formato (XX) XXXXX-XXXX")
    private String telefone;

    @Valid
    private CnpjVO cnpj;

    private String responsavel;
}