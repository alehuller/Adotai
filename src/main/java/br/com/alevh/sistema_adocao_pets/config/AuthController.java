package br.com.alevh.sistema_adocao_pets.config;

import br.com.alevh.sistema_adocao_pets.data.dto.v1.OngDTO;
import br.com.alevh.sistema_adocao_pets.service.OngService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alevh.sistema_adocao_pets.data.dto.security.LoginDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.security.RegistroDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.UsuarioDTO;
import br.com.alevh.sistema_adocao_pets.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@ToString
public class AuthController {

    private final UsuarioService usuarioService;

    private final OngService ongService;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    // auth de user
    @PostMapping("/user/login")
    public ResponseEntity loginUser(@RequestBody @Valid LoginDTO data) {
        return ResponseEntity.ok(usuarioService.logar(data));
    }

    @PostMapping("/user/register")
    public ResponseEntity registerUser(@RequestBody @Valid RegistroDTO data) {
        UsuarioDTO usuarioDTO = usuarioService.create(data);
        return ResponseEntity.ok().build();
    }

    // auth de ong
    @PostMapping("/ong/login")
    public ResponseEntity loginOng(@RequestBody @Valid LoginDTO data) {
        return ResponseEntity.ok(ongService.logar(data));
    }

    @PostMapping("/ong/register")
    public ResponseEntity registerOng(@RequestBody @Valid OngDTO data) {
        OngDTO ongDTO = ongService.create(data);
        return ResponseEntity.ok().build();
    }
}
