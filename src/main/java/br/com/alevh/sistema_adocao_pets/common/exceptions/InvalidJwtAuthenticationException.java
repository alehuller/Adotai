package br.com.alevh.sistema_adocao_pets.common.exceptions;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;

/**
 * Classe de exceção para autenticação JWT inválida.
 */
public class InvalidJwtAuthenticationException extends AuthenticationCredentialsNotFoundException {
    private static final long serialVersionUID = 1L;
   
    public InvalidJwtAuthenticationException(String ex) {
        super(ex);
    }
}
