package br.com.alevhvm.adotai.ong.exception;

import java.io.IOException;

public class ConversaoJsonParaRedeException extends RuntimeException{
    public ConversaoJsonParaRedeException(String message, IOException e) {
        super(message, e);
    }
}
