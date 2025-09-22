package br.com.alevhvm.adotai.ong.exception;

import java.io.IOException;

public class ConversaoJsonParaEnderecoException extends RuntimeException{
    public ConversaoJsonParaEnderecoException(String message, IOException e) {
        super(message, e);
    }
}
