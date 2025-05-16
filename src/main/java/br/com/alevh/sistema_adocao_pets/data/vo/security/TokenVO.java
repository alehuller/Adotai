package br.com.alevh.sistema_adocao_pets.data.vo.security;

import java.io.Serializable;
import java.util.Date;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class TokenVO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String nome;
    private Boolean authenticated;
    private Date created;
    private Date expiration;
    private String accessToken;
    private String refreshToken;
    
    public TokenVO(String nome, Boolean authenticated, Date created, Date expiration, String accessToken,
            String refreshToken) {
        this.nome = nome;
        this.authenticated = authenticated;
        this.created = created;
        this.expiration = expiration;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
