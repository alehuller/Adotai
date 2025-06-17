package br.com.alevh.sistema_adocao_pets.service.auth;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

@Service
public class TokenBlackListService {

    private final Set<String> blacklist = new HashSet<>();

    public void addToBlacklist(String token) {
        blacklist.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }
}
