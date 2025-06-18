package br.com.alevh.sistema_adocao_pets.service.auth;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class TokenBlackListService {

    private final Set<String> blacklist = new HashSet<>();

    public void addToBlacklist(HttpServletRequest request) {
        String token = extractToken(request);  
        blacklist.add(token);
    }

    public boolean isTokenBlacklisted(String token) {
        return blacklist.contains(token);
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new RuntimeException("Token não encontrado na requisição");
    }
}
