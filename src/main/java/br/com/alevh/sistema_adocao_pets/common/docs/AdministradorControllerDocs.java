package br.com.alevh.sistema_adocao_pets.common.docs;

import java.util.Map;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.alevh.sistema_adocao_pets.administrador.dto.AdministradorDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

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
    ResponseEntity<PagedModel<EntityModel<AdministradorDTO>>> listarAdministradores(int page, int size,
            String direction);

    @Operation(summary = "Retorna o administrador de id especificado", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdministradorDTO.class)))),
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    AdministradorDTO acharAdministradorPorId(Long id);

    @Operation(summary = "Retorna o administrador pelo nome de usuário pesquisado", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdministradorDTO.class)))),
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    AdministradorDTO acharAdministradorPorNomeUsuario(@PathVariable(value = "nomeUsuario") String nomeUsuario);

    @Operation(summary = "Atualiza o administrador", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdministradorDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    AdministradorDTO atualizarAdministrador(String nomeUsuario, AdministradorDTO administrador);

    @Operation(summary = "Atualização parcial do administrador", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdministradorDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    ResponseEntity<AdministradorDTO> atualizarParcialAdministrador(String nomeUsuario, Map<String, Object> updates);

    @Operation(summary = "Apaga o administrador de nome especificado", responses = {
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    ResponseEntity<?> deletarPorNomeUsuario(String nomeUsuario);
}
