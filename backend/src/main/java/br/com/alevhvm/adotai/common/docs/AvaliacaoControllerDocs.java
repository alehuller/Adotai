package br.com.alevhvm.adotai.common.docs;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.alevhvm.adotai.avaliacao.dto.AvaliacaoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

public interface AvaliacaoControllerDocs {
    @Operation(summary = "Retorna a avaliação de ID especificado", 
        responses = {
            @ApiResponse(responseCode = "200", description = "Success", 
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = AvaliacaoDTO.class))),
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    ResponseEntity<AvaliacaoDTO> acharAvaliacaoPorId(
        @Parameter(description = "ID da avaliação a ser buscada") 
        @PathVariable("id") Long id);


    @Operation(summary = "Registra uma nova avaliação", 
        responses = {
            @ApiResponse(responseCode = "201", description = "Created", 
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = AvaliacaoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    ResponseEntity<AvaliacaoDTO> registrarAvaliacao(
        @Parameter(description = "Dados da nova avaliação a ser registrada") 
        @RequestBody @Valid AvaliacaoDTO avaliacaoDTO);
}
