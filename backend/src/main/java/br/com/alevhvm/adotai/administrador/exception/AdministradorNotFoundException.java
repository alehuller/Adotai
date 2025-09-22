package br.com.alevhvm.adotai.administrador.exception;

public class AdministradorNotFoundException extends RuntimeException{
    public AdministradorNotFoundException(String message) {
        super(message);
    }
}
