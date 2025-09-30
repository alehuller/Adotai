package br.com.alevhvm.adotai.common.model;

import br.com.alevhvm.adotai.auth.enums.Roles;
import br.com.alevhvm.adotai.common.enums.StatusConta;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// não vira uma tabela no banco
@MappedSuperclass
@Getter
@Setter
public abstract class PerfilBase {

    @Column(name = "nome", nullable = false, length = 80)
    private String nome;

    @Column(name = "nome_usuario", nullable = false, unique = true, length = 80)
    private String nomeUsuario;

    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    @Column(name = "senha", nullable = false, length = 255)
    private String senha;

    @Column(name = "foto_perfil", nullable = true, length = 255)
    private String fotoPerfil;

    @Column(name = "cell", nullable = false, unique = true, length = 15)
    private String cell;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 255, columnDefinition = "varchar default 0")
    private Roles role;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusConta status = StatusConta.ATIVA;
}
