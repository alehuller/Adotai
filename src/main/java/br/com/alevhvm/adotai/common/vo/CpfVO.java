package br.com.alevhvm.adotai.common.vo;

import java.io.Serializable;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.hateoas.RepresentationModel;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CpfVO extends RepresentationModel<CpfVO> implements Serializable {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "O CPF é obrigatório")
    @CPF
    private String cpf;

    public CpfVO(String cpf) {
        this.cpf = cpf;
    }
}
