package br.com.alevh.sistema_adocao_pets.config;

import br.com.alevh.sistema_adocao_pets.data.dto.security.AuthDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.security.LoginResponseDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.security.RegistroDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.UsuarioDTO;
import br.com.alevh.sistema_adocao_pets.model.Usuario;
import br.com.alevh.sistema_adocao_pets.repository.UsuarioRepository;
import br.com.alevh.sistema_adocao_pets.service.UsuarioService;
import br.com.alevh.sistema_adocao_pets.util.UsuarioRole;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@ToString
public class AuthController {

    private final UsuarioService usuarioService;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthDTO data){
        // credenciais do spring security
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());

        // autentica de forma milagrosa as credenciais
        var auth = this.authenticationManager.authenticate(usernamePassword);

        //
        var token = tokenService.generateToken((Usuario) auth.getPrincipal());

        // exceções são lançadas pelo authentication manager na classe security config
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody @Valid RegistroDTO data){
        // se encontrar o usuario no bd retorna badrequest
        if(usuarioService.existsUsuarioWithEmail(data.getEmail())){
            return ResponseEntity.badRequest().build();
        }
        UsuarioDTO usuarioDTO = usuarioService.create(data);
        return ResponseEntity.ok().build();
    }
}
