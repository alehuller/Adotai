package br.com.alevhvm.adotai.auth.controller;

import br.com.alevhvm.adotai.auth.dto.LoginDTO;
import br.com.alevhvm.adotai.auth.dto.TokenDTO;
import br.com.alevhvm.adotai.administrador.dto.AdministradorDTO;
import br.com.alevhvm.adotai.ong.dto.OngDTO;
import br.com.alevhvm.adotai.administrador.service.AdministradorService;
import br.com.alevhvm.adotai.ong.service.OngService;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.alevhvm.adotai.auth.dto.RegistroDTO;
import br.com.alevhvm.adotai.usuario.dto.UsuarioDTO;
import br.com.alevhvm.adotai.usuario.service.UsuarioService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import br.com.alevhvm.adotai.auth.service.TokenBlackListService;
import br.com.alevhvm.adotai.auth.service.TokenService;
import br.com.alevhvm.adotai.common.docs.AuthControllerDocs;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@ToString
@Tag(name = "Autenticação", description = "Endpoints para Login, Registro e Logout de Administradores, Usuários e ONGs.")
public class AuthController implements AuthControllerDocs{

    private final AdministradorService administradorService;

    private final UsuarioService usuarioService;

    private final OngService ongService;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    private final TokenBlackListService tokenBlackListService;

    // auth de user
    @PostMapping("/user/login")
    public ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO data) {
        return ResponseEntity.ok(usuarioService.logar(data));
    }

    @PostMapping("/user/register")
    public ResponseEntity<UsuarioDTO> register(@RequestBody @Valid RegistroDTO data) {
        UsuarioDTO usuarioDTO = usuarioService.create(data);

        URI location = ServletUriComponentsBuilder
            .fromUriString("/api/v1/usuarios/id/{id}")
            .buildAndExpand(usuarioDTO.getKey())
            .toUri();
        
        return ResponseEntity.created(location).body(usuarioDTO);
    }

    // auth de ong
    @PostMapping("/ong/login")
    public ResponseEntity<TokenDTO> loginOng(@RequestBody @Valid LoginDTO data) {
        return ResponseEntity.ok(ongService.logar(data));
    }

    @PostMapping("/ong/register")
    public ResponseEntity<OngDTO> registerOng(@RequestBody @Valid OngDTO data) {
    OngDTO ongDTO = ongService.create(data);

    URI location = ServletUriComponentsBuilder
        .fromUriString("/api/v1/ongs/id/{id}")
        .buildAndExpand(ongDTO.getKey())
        .toUri();

    return ResponseEntity.created(location).body(ongDTO);
}

    // auth de adm
    @PostMapping("/admin/login")
    public ResponseEntity<TokenDTO> loginAdm(@RequestBody @Valid LoginDTO data) {
        return ResponseEntity.ok(administradorService.logar(data));
    }

    @PostMapping("/admin/register")
    public ResponseEntity<AdministradorDTO> registerAdm(@RequestBody @Valid AdministradorDTO data) {
    AdministradorDTO administradorDTO = administradorService.create(data);

    URI location = ServletUriComponentsBuilder
        .fromUriString("/api/v1/administradores/id/{id}")
        .buildAndExpand(administradorDTO.getKey())
        .toUri();

    return ResponseEntity.created(location).body(administradorDTO);
}

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/signout")
    public ResponseEntity<String> signOut(HttpServletRequest request) {
        tokenBlackListService.addToBlacklist(request);
        return ResponseEntity.ok("Logout realizado com sucesso");
    }
}
