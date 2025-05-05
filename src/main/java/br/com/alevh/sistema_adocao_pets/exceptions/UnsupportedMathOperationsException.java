package br.com.alevh.sistema_adocao_pets.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class UnsupportedMathOperationsException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public UnsupportedMathOperationsException(String message) {
        super(message);
    }
}
