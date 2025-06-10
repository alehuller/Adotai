package br.com.alevh.sistema_adocao_pets.model;
import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "usuario")
public class Usuario implements UserDetails {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(name = "nome", nullable = false, length = 255)
    private String nome;

    @Column(name = "nome_usuario", nullable = false, unique = true, length = 255)
    private String nomeUsuario;

    @Column(name = "email", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "senha", nullable = false, length = 255)
    private String senha;

    @Column(name = "foto_perfil", nullable = true, length = 255)
    private String fotoPerfil;

    @Column(name = "cell", nullable = false, unique = true, length = 11)
    private String cell;

    @Column(name = "cpf", nullable = false, unique = true, length = 11)
    private String cpf;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
}

    @Override
    public String getPassword() {
        return this.senha;    
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}