package br.com.alevhvm.adotai.common.docs;

import java.util.Map;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.alevhvm.adotai.adocao.dto.AdocaoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

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
    ResponseEntity<PagedModel<EntityModel<AdocaoDTO>>> listarAdocoes(
            @Parameter(description = "Número da página (padrão 0)") int page,
            @Parameter(description = "Tamanho da página (padrão 10)") int size,
            @Parameter(description = "Direção da ordenação: asc ou desc (padrão asc)") String direction);


    @Operation(summary = "Retorna a adoção de id especificado", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdocaoDTO.class)))
            }),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    ResponseEntity<AdocaoDTO> acharAdocaoPorId(
            @Parameter(description = "ID da adoção") 
            @PathVariable Long id);

    @Operation(summary = "Registra uma adoção", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdocaoDTO.class)))
            }),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    ResponseEntity<AdocaoDTO> registrarAdocao(
            @Parameter(description = "Dados da adoção para registro") 
            @RequestBody @Valid AdocaoDTO adocao);

    @Operation(summary = "Atualiza a adoção", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdocaoDTO.class)))
            }),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    ResponseEntity<AdocaoDTO> atualizarAdocao(
            @Parameter(description = "ID da adoção a ser atualizada") 
            @PathVariable Long id,
            @Parameter(description = "Dados atualizados da adoção") 
            @RequestBody @Valid AdocaoDTO adocao);

    @Operation(summary = "Atualização parcial de Adocao", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdocaoDTO.class)))
            }),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    ResponseEntity<AdocaoDTO> atualizarParcialAdocao(
            @Parameter(description = "ID da adoção a ser atualizada") 
            @PathVariable Long id,
            @Parameter(description = "Campos e valores para atualização parcial") 
            @RequestBody Map<String, Object> updates);

    @Operation(summary = "Apaga a adoção de id especificado", responses = {
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
    })
    ResponseEntity<?> deletarPorId(
            @Parameter(description = "ID da adoção a ser deletada") 
            @PathVariable Long id);
}
