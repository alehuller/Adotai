package br.com.alevhvm.adotai.ong.model;

import br.com.alevhvm.adotai.common.model.PerfilBase;
import jakarta.persistence.*;

import br.com.alevhvm.adotai.common.vo.EnderecoVO;
import br.com.alevhvm.adotai.common.vo.SiteVO;
import br.com.alevhvm.adotai.ong.converter.EnderecoConverter;
import br.com.alevhvm.adotai.ong.converter.SiteConverter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Entity
@Table(name = "ong")

public class Ong extends PerfilBase {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idOng;

    @Convert(converter = EnderecoConverter.class)
    @Column(name = "endereco", nullable = false, columnDefinition = "TEXT")
    private EnderecoVO endereco;

    @Column(name = "cnpj", nullable = false, unique = true, length = 18)
    private String cnpj;

    @Column(name = "responsavel", nullable = false, length = 255)
    private String responsavel;

    @Column(name = "descricao", columnDefinition = "TEXT")
    private String descricao;

    @Convert(converter = SiteConverter.class)
    @Column(name = "site", nullable = true, columnDefinition = "TEXT")
    private SiteVO site;

}
