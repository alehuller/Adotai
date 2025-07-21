package br.com.alevhvm.adotai.common.vo;

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
public class DescricaoVO extends RepresentationModel<DescricaoVO> implements Serializable {
    private static final long serialVersionUID = 1L;

    private String geral;

    private String historicoSaude;

    private String vacinacao;
}
