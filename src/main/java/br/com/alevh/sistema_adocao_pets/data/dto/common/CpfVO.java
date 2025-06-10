package br.com.alevh.sistema_adocao_pets.data.dto.common;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CpfVO extends RepresentationModel<CpfVO> implements Serializable{
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "O CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "O CPF deve conter 11 dígitos numéricos")
    private String cpf;

    //Regex para validação do CPF:
    //CPF: "^((\d{3}).(\d{3}).(\d{3})-(\d{2}))*$"
}
