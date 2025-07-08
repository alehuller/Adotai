package br.com.alevh.sistema_adocao_pets.model;

import java.io.Serializable;
import java.time.LocalDate;

import br.com.alevh.sistema_adocao_pets.data.dto.common.DescricaoVO;
import br.com.alevh.sistema_adocao_pets.enums.StatusAnimal;
import br.com.alevh.sistema_adocao_pets.serialization.converter.DescricaoConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
@Table(name = "animal")
public class Animal implements Serializable{
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idAnimal;

    @Column(name = "nome", nullable = false, length = 80)
    private String nome;

    @Column(name = "especie", nullable = false, length = 50)
    private String especie;

    @Column(name = "raca", nullable = false, length = 50)
    private String raca;

    @Column(name = "data_nascimento", nullable = false)
    private LocalDate dataNascimento;

    @Column(name = "foto", length = 255)
    private String foto;

    @Convert(converter = DescricaoConverter.class)
    @Column(name = "descricao", columnDefinition = "TEXT")
    private DescricaoVO descricao;

    @Column(name = "porte", nullable = false, length = 50)
    private String porte;

    @Column(name = "sexo", nullable = false, length = 10)
    private String sexo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 30)
    private StatusAnimal status;

    //varios animais podem estar associados a uma única ong
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "ong_id", nullable = false)
    private Ong ong;
}
