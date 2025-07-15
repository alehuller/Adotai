package br.com.alevhvm.adotai.usuario.model;

import br.com.alevhvm.adotai.animal.model.Animal;
import br.com.alevhvm.adotai.common.model.PerfilBase;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "usuario")
public class Usuario extends PerfilBase {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idUsuario;

    @Column(name = "cpf", nullable = false, unique = true, length = 14)
    private String cpf;

    @ManyToMany(fetch = FetchType.EAGER) 
    @JoinTable(
        name = "usuario_animais_favoritos",
        joinColumns = @JoinColumn(name = "usuario_id"), 
        inverseJoinColumns = @JoinColumn(name = "animal_id") 
    )
    private Set<Animal> animaisFavoritos = new HashSet<>();
}