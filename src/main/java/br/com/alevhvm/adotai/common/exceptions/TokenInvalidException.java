package br.com.alevhvm.adotai.common.exceptions;

import org.springframework.security.core.AuthenticationException;

public class TokenInvalidException extends AuthenticationException {
    public TokenInvalidException(String msg) {
        super(msg);
    }

    public TokenInvalidException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
