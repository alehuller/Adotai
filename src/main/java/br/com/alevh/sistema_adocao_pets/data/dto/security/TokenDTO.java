package br.com.alevh.sistema_adocao_pets.data.dto.security;

import java.io.Serializable;
import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class TokenDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private Boolean authenticated;
    private Date created;
    private Date expiration;
    private String accessToken;
    private String refreshToken;
    
    public TokenDTO(String nome, Boolean authenticated, Date created, Date expiration, String accessToken,
            String refreshToken) {
        this.nome = nome;
        this.authenticated = authenticated;
        this.created = created;
        this.expiration = expiration;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}