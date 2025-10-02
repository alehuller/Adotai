package br.com.alevhvm.adotai.auth.model;

import java.util.Collection;

import org.springframework.data.annotation.Immutable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.alevhvm.adotai.auth.enums.Roles;
import br.com.alevhvm.adotai.common.enums.StatusConta;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;

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

    @Enumerated(EnumType.STRING)
    private StatusConta status;

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

    @Override
    public boolean isEnabled() {
        return this.status == StatusConta.ATIVA;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status != StatusConta.BLOQUEADA;
    }
}
