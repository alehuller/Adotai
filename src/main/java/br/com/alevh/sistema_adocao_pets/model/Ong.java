package br.com.alevh.sistema_adocao_pets.model;

import java.util.Collection;
import java.util.List;

import jakarta.persistence.*;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.alevh.sistema_adocao_pets.data.dto.common.EnderecoVO;
import br.com.alevh.sistema_adocao_pets.data.dto.common.SiteVO;
import br.com.alevh.sistema_adocao_pets.serialization.converter.EnderecoConverter;
import br.com.alevh.sistema_adocao_pets.serialization.converter.SiteConverter;
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

public class Ong extends PerfilBase{
    private static final long serialVersionUID = 1L;

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
