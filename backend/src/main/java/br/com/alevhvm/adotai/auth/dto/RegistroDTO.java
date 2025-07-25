package br.com.alevhvm.adotai.auth.dto;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.alevhvm.adotai.common.vo.CpfVO;
import br.com.alevhvm.adotai.usuario.dto.UsuarioDTO;
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
public class RegistroDTO extends RepresentationModel<UsuarioDTO> implements Serializable {
    @NotBlank(message = "O e-mail é obrigatório")
    @NotNull(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido. Insira um endereço de e-mail válido")
    String email;

    @NotBlank(message = "A senha de usuário é obrigatório")
    @NotNull(message = "A senha de usuário é obrigatório")
    @JsonProperty(value = "senha")
    String password;

    @NotBlank(message = "O e-mail é obrigatório")
    @NotNull(message = "O email é obrigatório")
    String nome;

    @NotBlank(message = "O nome de usuário é obrigatório")
    @NotNull(message = "O nome de usuário é obrigatório")
    @Pattern(regexp = "^\\S+$", message = "O nome de usuário não pode conter espaços")
    String nomeUsuario;

    @NotBlank(message = "A foto de usuário é obrigatório")
    @NotNull(message = "A foto de usuário é obrigatório")
    String fotoPerfil;

    @NotBlank(message = "O celular de usuário é obrigatório")
    @NotNull(message = "O celular de usuário é obrigatório")
    @JsonProperty(value = "celular")
    @Pattern(regexp = "^\\(\\d{2}\\) \\d{5}-\\d{4}$", message = "O celular deve estar no formato (XX) XXXXX-XXXX")
    String cell;

    @Valid
    @NotNull(message = "O cpf de usuário é obrigatório")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private CpfVO cpf;
}
