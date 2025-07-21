package br.com.alevhvm.adotai.adocao.model;

import java.io.Serializable;
import java.time.LocalDate;

import br.com.alevhvm.adotai.adocao.enums.StatusAdocao;
import br.com.alevhvm.adotai.animal.model.Animal;
import br.com.alevhvm.adotai.usuario.model.Usuario;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "adocao")
public class Adocao implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAdocao;

    @Column(name = "data_adocao", nullable = false)
    private LocalDate dataAdocao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 50)
    private StatusAdocao status;

    // do ponto de vista da adoção, cada registro está ligado a um único usuário,
    // mas o mesmo usuário pode aparecer em várias adoções.
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // muitas adoções podem estar associadas a um mesmo animal (por exemplo, em um
    // sistema que mantém histórico de tentativas de adoção, ou onde um animal é
    // devolvido e adotado novamente).
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "animal_id", nullable = false)
    private Animal animal;
}