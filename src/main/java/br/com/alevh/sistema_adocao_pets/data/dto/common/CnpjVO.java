package br.com.alevh.sistema_adocao_pets.data.dto.common;

import java.io.Serializable;

import org.hibernate.validator.constraints.br.CNPJ;
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
public class CnpjVO extends RepresentationModel<CnpjVO> implements Serializable{
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "O CNPJ é obrigatório")
    //@Pattern(regexp = "^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$", message = "O CNPJ deve estar no formato XX.XXX.XXX/XXXX-XX")
    @CNPJ
    private String cnpj;

    public CnpjVO(String cnpj) {
        this.cnpj = cnpj;
    }
}
