package br.com.alevhvm.adotai.ong.dto;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;

import br.com.alevhvm.adotai.common.vo.EnderecoVO;
import br.com.alevhvm.adotai.common.vo.SiteVO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@JsonPropertyOrder ({ "nome", "nomeUsuario", "email", "endereco", "cell", "descricao", "responsavel", "site", "fotoPerfil" })
public class OngUpdateDTO extends RepresentationModel<OngUpdateDTO> implements Serializable{
    private static final long serialVersionUID = 1L;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "idOng")
    @Mapping("idOng")
    private Long key;

    @NotBlank(message = "O nome da ong é obrigatório")
    @NotNull(message = "O nome da ong é obrigatório")
    private String nome;

    @NotBlank(message = "O nome de usuário da ong é obrigatório")
    @NotNull(message = "O nome de usuário da ong é obrigatório")
    @Pattern(regexp = "^\\S+$", message = "O nome de usuário não pode conter espaços")
    private String nomeUsuario;

    @NotBlank(message = "A foto de perfil da ong é obrigatória")
    @NotNull(message = "A foto de perfil da ong é obrigatória")
    private String fotoPerfil;

    @NotBlank(message = "O e-mail é obrigatório")
    @NotNull(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido. Insira um endereço de e-mail válido")
    private String email;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    @Valid
    @NotNull(message = "O endereço da ong é obrigatório")
    private EnderecoVO endereco;

    @NotBlank(message = "O celular da ong é obrigatório")
    @NotNull(message = "O celular da ong é obrigatório")
    @Pattern(regexp = "^\\(\\d{2}\\) \\d{5}-\\d{4}$", message = "O celular deve estar no formato (XX) XXXXX-XXXX")
    private String cell;

    @NotBlank(message = "O responsável pela ong é obrigatório")
    @NotNull(message = "O responsável pela ong é obrigatório")
    private String responsavel;

    @NotBlank(message = "O celular da ong é obrigatório")
    @NotNull(message = "O celular da ong é obrigatório")
    private String descricao;

    @Valid
    private SiteVO site;
}
