package br.com.alevh.sistema_adocao_pets.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alevh.sistema_adocao_pets.data.dto.security.AuthDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.security.RegistroDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.UsuarioDTO;
import br.com.alevh.sistema_adocao_pets.service.TokenBlackListService;
import br.com.alevh.sistema_adocao_pets.service.TokenService;
import br.com.alevh.sistema_adocao_pets.service.UsuarioService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@ToString
public class AuthController {

    private final UsuarioService usuarioService;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    private final TokenBlackListService tokenBlackListService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthDTO data) {
        return ResponseEntity.ok(usuarioService.logar(data));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegistroDTO data) {

        UsuarioDTO usuarioDTO = usuarioService.create(data);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/signout")
    public ResponseEntity<String> signOut(HttpServletRequest request) {
        String token = extractToken(request);
        tokenBlackListService.addToBlacklist(token);
        return ResponseEntity.ok("Logout realizado com sucesso");
    }

    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        throw new RuntimeException("Token não encontrado na requisição");
    }
}
