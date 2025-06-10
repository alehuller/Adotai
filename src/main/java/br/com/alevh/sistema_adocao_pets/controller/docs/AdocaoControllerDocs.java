package br.com.alevh.sistema_adocao_pets.controller.docs;

import java.util.Map;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;

import br.com.alevh.sistema_adocao_pets.data.dto.v1.AdocaoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface AdocaoControllerDocs {
     @Operation(summary = "Retorna todas as adoções", responses = {
        @ApiResponse(description = "Success", responseCode = "200", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdocaoDTO.class)))
        }),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
        @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    ResponseEntity<PagedModel<EntityModel<AdocaoDTO>>> listarAdocoes(int page, int size, String direction);

    @Operation(summary = "Retorna a adoção de id especificado", responses = {
        @ApiResponse(description = "Success", responseCode = "200", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdocaoDTO.class)))
        }),
        @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
        @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    AdocaoDTO acharAdocaoPorId(Long id);

    @Operation(summary = "Registra uma adoção", responses = {
        @ApiResponse(description = "Success", responseCode = "200", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdocaoDTO.class)))
        }),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    AdocaoDTO registrarAdocao(AdocaoDTO adocao);

    @Operation(summary = "Apaga a adoção de id especificado", responses = {
        @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
        @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    ResponseEntity<?> deletarPorId(Long id);

    @Operation(summary = "Atualiza a adoção", responses = {
        @ApiResponse(description = "Success", responseCode = "200", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdocaoDTO.class)))
        }),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
        @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    AdocaoDTO atualizarAdocao(Long id, AdocaoDTO adocao);

    @Operation(summary = "Atualização parcial de Adocao", responses = {
        @ApiResponse(description = "Success", responseCode = "200", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdocaoDTO.class)))
        }),
        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
        @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    ResponseEntity<AdocaoDTO> atualizarParcialAdocao(Long id, Map<String, Object> updates);
}
