package br.com.alevhvm.adotai.ong.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ConversaoEnderecoParaJsonException extends RuntimeException{
    public ConversaoEnderecoParaJsonException(String message, JsonProcessingException e) {
        super(message, e);
    }
}
