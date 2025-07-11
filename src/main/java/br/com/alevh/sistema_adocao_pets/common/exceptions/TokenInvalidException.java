package br.com.alevh.sistema_adocao_pets.common.exceptions;

import org.springframework.security.core.AuthenticationException;

public class TokenInvalidException extends AuthenticationException {
    public TokenInvalidException(String msg) {
        super(msg);
    }

    public TokenInvalidException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
