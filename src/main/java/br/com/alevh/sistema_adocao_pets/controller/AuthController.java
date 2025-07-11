package br.com.alevh.sistema_adocao_pets.controller;

import br.com.alevh.sistema_adocao_pets.data.dto.security.LoginDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.security.TokenDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.AdministradorDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.OngDTO;
import br.com.alevh.sistema_adocao_pets.service.AdministradorService;
import br.com.alevh.sistema_adocao_pets.service.OngService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import br.com.alevh.sistema_adocao_pets.data.dto.security.RegistroDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.UsuarioDTO;
import br.com.alevh.sistema_adocao_pets.service.UsuarioService;
import br.com.alevh.sistema_adocao_pets.service.auth.TokenBlackListService;
import br.com.alevh.sistema_adocao_pets.service.auth.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@ToString
public class AuthController {

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
        return ResponseEntity.ok(usuarioDTO);
    }

    // auth de ong
    @PostMapping("/ong/login")
    public ResponseEntity<TokenDTO> loginOng(@RequestBody @Valid LoginDTO data) {
        return ResponseEntity.ok(ongService.logar(data));
    }

    @PostMapping("/ong/register")
    public ResponseEntity<OngDTO> registerOng(@RequestBody @Valid OngDTO data) {
        OngDTO ongDTO = ongService.create(data);
        return ResponseEntity.ok(ongDTO);
    }

    // auth de adm
    @PostMapping("/admin/login")
    public ResponseEntity<TokenDTO> loginAdm(@RequestBody @Valid LoginDTO data) {
        return ResponseEntity.ok(administradorService.logar(data));
    }

    @PostMapping("/adminmaster/register")
    public ResponseEntity<AdministradorDTO> registerAdm(@RequestBody @Valid AdministradorDTO data) {
        AdministradorDTO administradorDTO = administradorService.create(data);
        return ResponseEntity.ok(administradorDTO);
    }

    @PostMapping("/signout")
    public ResponseEntity<String> signOut(HttpServletRequest request) {
        tokenBlackListService.addToBlacklist(request);
        return ResponseEntity.ok("Logout realizado com sucesso");
    }
}
