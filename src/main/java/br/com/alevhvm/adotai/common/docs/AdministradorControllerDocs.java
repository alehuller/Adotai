package br.com.alevhvm.adotai.common.docs;

import java.util.Map;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.alevhvm.adotai.administrador.dto.AdministradorDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

public interface AdministradorControllerDocs {
    @Operation(summary = "Retorna todos os administradores", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdministradorDTO.class)))
            }),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    ResponseEntity<PagedModel<EntityModel<AdministradorDTO>>> listarAdministradores(
            @Parameter(description = "Número da página (padrão 0)")int page, 
            @Parameter(description = "Tamanho da página (padrão 10)")int size,
            @Parameter(description = "Direção da ordenação: asc ou desc (padrão asc)")String direction);

    @Operation(summary = "Retorna o administrador de id especificado", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdministradorDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    ResponseEntity<AdministradorDTO> acharAdministradorPorId(
    @Parameter(description = "ID do administrador")    
    Long id);

    @Operation(summary = "Retorna o administrador pelo nome de usuário pesquisado", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdministradorDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    ResponseEntity<AdministradorDTO> acharAdministradorPorNomeUsuario(
    @Parameter(description = "Nome de usuário do administrador") 
    @PathVariable String nomeUsuario);

    @Operation(summary = "Atualiza o administrador", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdministradorDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    ResponseEntity<AdministradorDTO> atualizarAdministrador(
            @Parameter(description = "Nome de usuário do administrador a ser atualizado") 
            @PathVariable String nomeUsuario,
            @Parameter(description = "Dados atualizados do administrador") 
            @RequestBody @Valid AdministradorDTO administrador);

    @Operation(summary = "Atualização parcial do administrador", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdministradorDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    ResponseEntity<AdministradorDTO> atualizarParcialAdministrador(
            @Parameter(description = "Nome de usuário do administrador a ser atualizado") 
            @PathVariable String nomeUsuario,
            @Parameter(description = "Campos e valores para atualização parcial") 
            @RequestBody Map<String, Object> updates);

    @Operation(summary = "Apaga o administrador de nome especificado", responses = {
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    ResponseEntity<?> deletarPorNomeUsuario(
            @Parameter(description = "Nome de usuário do administrador a ser deletado") 
            @PathVariable String nomeUsuario);
}
