package br.com.alevh.sistema_adocao_pets.data.dto.v1;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.github.dozermapper.core.Mapping;

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
@JsonPropertyOrder({})
public class AdministradorDTO {
    
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "idAdministrador")
    @Mapping("idAdministrador")
    private Long key;

    @NotBlank(message = "O nome é obrigatório")
    @NotNull(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O nome de usuário é obrigatório")
    @NotNull(message = "O nome de usuário é obrigatório")
    @Pattern(regexp = "^\\S+$", message = "O nome de usuário não pode conter espaços")
    private String nomeUsuario;

    @NotBlank(message = "O e-mail é obrigatório")
    @NotNull(message = "O e-mail é obrigatório")
    @Email(message = "E-mail inválido. Insira um endereço de e-mail válido")
    private String email;

    @NotBlank(message = "A senha de usuário é obrigatório")
    @NotNull(message = "A senha de usuário é obrigatório")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String senha;

    private String fotoPerfil;
}
