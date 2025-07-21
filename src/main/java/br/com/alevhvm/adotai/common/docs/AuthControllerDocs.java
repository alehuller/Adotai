package br.com.alevhvm.adotai.common.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.alevhvm.adotai.administrador.dto.AdministradorDTO;
import br.com.alevhvm.adotai.auth.dto.LoginDTO;
import br.com.alevhvm.adotai.auth.dto.RegistroDTO;
import br.com.alevhvm.adotai.auth.dto.TokenDTO;
import br.com.alevhvm.adotai.ong.dto.OngDTO;
import br.com.alevhvm.adotai.usuario.dto.UsuarioDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

public interface AuthControllerDocs {

    @Operation(summary = "Realiza login de usuário", responses = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso", content = @Content(schema = @Schema(implementation = TokenDTO.class))),
            @ApiResponse(responseCode = "400", description = "Credenciais inválidas", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content)
    })
    ResponseEntity<TokenDTO> login(@RequestBody @Valid LoginDTO data);

    @Operation(summary = "Registra um novo usuário", responses = {
            @ApiResponse(responseCode = "201", description = "Usuário registrado com sucesso", content = @Content(schema = @Schema(implementation = UsuarioDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    ResponseEntity<UsuarioDTO> register(@RequestBody @Valid RegistroDTO data);

    @Operation(summary = "Realiza login de ONG", responses = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso", content = @Content(schema = @Schema(implementation = TokenDTO.class))),
            @ApiResponse(responseCode = "400", description = "Credenciais inválidas", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content)
    })
    ResponseEntity<TokenDTO> loginOng(@RequestBody @Valid LoginDTO data);

    @Operation(summary = "Registra uma nova ONG", responses = {
            @ApiResponse(responseCode = "201", description = "ONG registrada com sucesso", content = @Content(schema = @Schema(implementation = OngDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    ResponseEntity<OngDTO> registerOng(@RequestBody @Valid OngDTO data);

    @Operation(summary = "Realiza login de administrador", responses = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso", content = @Content(schema = @Schema(implementation = TokenDTO.class))),
            @ApiResponse(responseCode = "400", description = "Credenciais inválidas", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content)
    })
    ResponseEntity<TokenDTO> loginAdm(@RequestBody @Valid LoginDTO data);

    @Operation(summary = "Registra um novo administrador", responses = {
            @ApiResponse(responseCode = "201", description = "Administrador registrado com sucesso", content = @Content(schema = @Schema(implementation = AdministradorDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos", content = @Content)
    })
    ResponseEntity<AdministradorDTO> registerAdm(@RequestBody @Valid AdministradorDTO data);

    @Operation(summary = "Realiza logout do usuário", responses = {
            @ApiResponse(responseCode = "200", description = "Logout realizado com sucesso", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "401", description = "Não autorizado", content = @Content)
    })
    ResponseEntity<String> signOut(HttpServletRequest request);
}

