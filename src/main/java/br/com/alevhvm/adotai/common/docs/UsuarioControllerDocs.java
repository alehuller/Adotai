package br.com.alevhvm.adotai.common.docs;

import java.util.Map;

import br.com.alevhvm.adotai.auth.dto.RegistroDTO;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.alevhvm.adotai.adocao.dto.AdocaoDTO;
import br.com.alevhvm.adotai.usuario.dto.UsuarioDTO;
import br.com.alevhvm.adotai.usuario.dto.UsuarioUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

public interface UsuarioControllerDocs {
    @Operation(summary = "Retorna todos os usuários", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    ResponseEntity<PagedModel<EntityModel<UsuarioDTO>>> listarUsuarios(
            @Parameter(description = "Número da página (padrão 0)") int page,
            @Parameter(description = "Tamanho da página (padrão 10)") int size,
            @Parameter(description = "Direção da ordenação: asc ou desc (padrão asc)") String direction);


    @Operation(summary = "Retorna o usuário de id especificado", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioDTO.class)))),
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    UsuarioDTO acharUsuarioPorId(
            @Parameter(description = "ID do usuário") 
            @PathVariable Long id);

    @Operation(summary = "Retorna o usuário pelo nome de usuário pesquisado", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioDTO.class)))),
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    UsuarioDTO acharUsuarioPorNomeUsuario(
            @Parameter(description = "Nome de usuário do usuário") 
            @PathVariable("nomeUsuario") String nomeUsuario);

    @Operation(summary = "Retorna todas as adoções de um usuário específico", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdocaoDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    ResponseEntity<PagedModel<EntityModel<AdocaoDTO>>> listarAdocoesPorNomeUsuario(
            @Parameter(description = "Nome de usuário do usuário") String nomeUsuario,
            @Parameter(description = "Número da página (padrão 0)") int page,
            @Parameter(description = "Tamanho da página (padrão 10)") int size,
            @Parameter(description = "Direção da ordenação: asc ou desc (padrão asc)") String direction);

    @Operation(summary = "Registra um usuário", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    UsuarioDTO registrarUsuario(
            @Parameter(description = "Dados do novo usuário") 
            @RequestBody @Valid RegistroDTO usuario);

    @Operation(summary = "Atualiza o usuário", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    UsuarioDTO atualizarUsuario(
            @Parameter(description = "Nome de usuário do usuário a ser atualizado") 
            @PathVariable String nomeUsuario,
            @Parameter(description = "Dados atualizados do usuário") 
            @RequestBody @Valid UsuarioUpdateDTO usuario);

    @Operation(summary = "Atualização parcial do usuário", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    ResponseEntity<UsuarioDTO> atualizarParcialUsuario(
            @Parameter(description = "Nome de usuário do usuário a ser atualizado") 
            @PathVariable String nomeUsuario,
            @Parameter(description = "Campos e valores para atualização parcial") 
            @RequestBody Map<String, Object> updates);

    @Operation(summary = "Apaga o usuário de nome especificado", responses = {
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    ResponseEntity<?> deletarPorNomeUsuario(
            @Parameter(description = "Nome de usuário do usuário a ser deletado") 
            @PathVariable String nomeUsuario);
}