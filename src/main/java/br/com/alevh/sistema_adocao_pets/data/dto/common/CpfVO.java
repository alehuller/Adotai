package br.com.alevh.sistema_adocao_pets.data.dto.common;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CpfVO extends RepresentationModel<CpfVO> implements Serializable{
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "O CPF é obrigatório")
    //@Pattern(regexp = "^\\d{3}\\.\\d{3}\\.\\d{3}-\\d{2}$", message = "O CPF deve estar no formato XXX.XXX.XXX-XX")
    @CPF
    private String cpf;

    public CpfVO(String cpf) {
        this.cpf = cpf;
    }
}
