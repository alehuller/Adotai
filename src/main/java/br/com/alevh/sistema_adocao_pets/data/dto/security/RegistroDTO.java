package br.com.alevh.sistema_adocao_pets.data.dto.security;


import br.com.alevh.sistema_adocao_pets.data.dto.v1.UsuarioDTO;
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
    String cell;
    String cpf;
}
