package br.com.alevh.sistema_adocao_pets.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Classe de exceção para autenticação JWT inválida.
 */
public class InvalidJwtAuthenticationException extends AuthenticationCredentialsNotFoundException {
    private static final long serialVersionUID = 1L;
   
    public InvalidJwtAuthenticationException(String ex) {
        super(ex);
    }
}
