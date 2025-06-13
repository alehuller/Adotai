package br.com.alevh.sistema_adocao_pets.data.dto.security;


import br.com.alevh.sistema_adocao_pets.data.dto.common.CpfVO;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.UsuarioDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
public class RegistroDTO extends RepresentationModel<UsuarioDTO> implements Serializable{
    String email;
    String password;
    String nome;
    String nomeUsuario;
    String fotoPerfil;
    String cell;
    @Valid
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private CpfVO cpf;
}
