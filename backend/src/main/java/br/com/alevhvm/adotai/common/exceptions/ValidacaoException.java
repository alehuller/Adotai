package br.com.alevhvm.adotai.common.exceptions;

import java.util.List;

public class ValidacaoException extends RuntimeException {
    private final List<String> erros;

    public ValidacaoException(List<String> erros) {
        super("Erro(s) de validação");
        this.erros = erros;
    }

    public List<String> getErros() {
        return erros;
    }
}
