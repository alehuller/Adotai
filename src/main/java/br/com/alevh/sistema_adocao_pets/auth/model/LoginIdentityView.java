package br.com.alevh.sistema_adocao_pets.auth.model;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.Immutable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.alevh.sistema_adocao_pets.auth.enums.Roles;

import java.util.Collection;

@Entity
@Table(name = "v_login_identity")
@Immutable
@Getter
public class LoginIdentityView implements UserDetails {
    @Id
    private Long id;

    private String email;

    private String senha;

    private String nomeUsuario;

    @Enumerated(EnumType.STRING)
    private Roles role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities().stream()
                .map(auth -> new SimpleGrantedAuthority("ROLE_" + auth))
                .toList();
    }

    @Override
    public String getPassword() {
        return getSenha();
    }

    @Override
    public String getUsername() {
        return getEmail();
    }
}
