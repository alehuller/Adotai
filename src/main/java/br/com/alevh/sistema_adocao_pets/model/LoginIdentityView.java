package br.com.alevh.sistema_adocao_pets.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import org.springframework.data.annotation.Immutable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.alevh.sistema_adocao_pets.security.Roles;

import java.util.Collection;
import java.util.List;

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

    private Roles role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role));
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
