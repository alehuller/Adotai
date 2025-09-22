package br.com.alevhvm.adotai.animal.exception;

public class AnimalNotFoundException extends RuntimeException{
    public AnimalNotFoundException(String message) {
        super(message);
    }
    
}
