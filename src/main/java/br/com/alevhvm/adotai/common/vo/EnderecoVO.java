package br.com.alevhvm.adotai.common.vo;

import java.io.Serializable;

import org.springframework.hateoas.RepresentationModel;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EnderecoVO extends RepresentationModel<EnderecoVO> implements Serializable {
    private static final long serialVersionUID = 1L;

    private String logradouro;

    @NotBlank(message = "Número é obrigatório")
    private String numero;

    private String complemento;

    private String bairro;

    private String cidade;

    @Size(min = 2, max = 2, message = "O estado deve ter exatamente 2 caracteres (ex: SP)")
    private String estado;

    @NotBlank(message = "CEP é obrigatório")
    @Size(min = 8, max = 10, message = "CEP deve ter entre 8 e 10 dígitos")
    private String cep;

    public EnderecoVO(String logradouro, String numero, String complemento, String bairro,
                      String cidade, String estado, String cep) {
        this.logradouro = logradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }
}