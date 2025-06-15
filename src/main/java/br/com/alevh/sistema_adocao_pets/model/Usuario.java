package br.com.alevh.sistema_adocao_pets.model;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import br.com.alevh.sistema_adocao_pets.util.UsuarioRole;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;

import static jakarta.persistence.InheritanceType.JOINED;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "usuario")
public class Usuario extends PerfilBase implements UserDetails {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(name = "cpf", nullable = false, unique = true, length = 14)
    private String cpf;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return getSenha();
    }

    //
    @Override
    public String getUsername() {
        return getEmail();
    }
}