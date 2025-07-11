package br.com.alevh.sistema_adocao_pets.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Roles {
    USER(List.of("USER")),
    ONG(List.of("ONG")),
    ADMIN(List.of("ADMIN", "USER", "ONG")),
    ADMINMASTER(List.of("ADMINMASTER", "ADMIN", "USER", "ONG"));

    private final List<String> authorities;

    Roles(List<String> authorities) {
        this.authorities = authorities;
    }

    public Collection<GrantedAuthority> getAuthorities() {
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
