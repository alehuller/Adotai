package br.com.alevh.sistema_adocao_pets.model;

import br.com.alevh.sistema_adocao_pets.util.UsuarioRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

// não vira uma tabela no banco
@MappedSuperclass
@Getter
@Setter
public abstract class PerfilBase {

    private static final long serialVersionUID = 1L;

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

    @Column(name = "role", nullable = false, length = 255, columnDefinition = "smallint default 0")
    private UsuarioRole role;
}
