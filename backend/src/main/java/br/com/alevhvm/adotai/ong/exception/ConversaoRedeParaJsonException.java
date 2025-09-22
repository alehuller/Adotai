package br.com.alevhvm.adotai.ong.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class ConversaoRedeParaJsonException extends RuntimeException{
    public ConversaoRedeParaJsonException(String message, JsonProcessingException e) {
        super(message, e);
    }
}
