package br.com.alevh.sistema_adocao_pets.data.dto.common;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class DescricaoVO extends RepresentationModel<DescricaoVO> implements Serializable{
    private static final long serialVersionUID = 1L;

    private String geral;

    private String historicoSaude;

    private String vacinacao;
}
