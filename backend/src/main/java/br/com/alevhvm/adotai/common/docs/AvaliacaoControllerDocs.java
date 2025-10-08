package br.com.alevhvm.adotai.common.docs;

import java.util.List;
import java.util.Map;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import br.com.alevhvm.adotai.avaliacao.dto.AvaliacaoDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

public interface AvaliacaoControllerDocs {

    @Operation(summary = "Lista todas as avaliações cadastradas", 
        responses = {
            @ApiResponse(responseCode = "200", description = "Success", 
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = AvaliacaoDTO.class))),
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    ResponseEntity<PagedModel<EntityModel<AvaliacaoDTO>>> listarTodasAvaliacoes(
        @Parameter(description = "Número da página para paginação") int page,
        @Parameter(description = "Quantidade de registros por página") int size,
        @Parameter(description = "Direção da ordenação (asc ou desc)") String direction);

    @Operation(summary = "Lista todas as avaliações de uma ONG específica", 
        responses = {
            @ApiResponse(responseCode = "200", description = "Success", 
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = AvaliacaoDTO.class))),
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    ResponseEntity<PagedModel<EntityModel<AvaliacaoDTO>>> listarAvaliacoesDeUmaOng(
        @Parameter(description = "ID da ONG a ser consultada") 
        @PathVariable("id") Long id,
        @Parameter(description = "Número da página para paginação", example = "0") 
        @RequestParam(value = "page", defaultValue = "0") int page,
        @Parameter(description = "Quantidade de registros por página", example = "10") 
        @RequestParam(value = "size", defaultValue = "10") int size,
        @Parameter(description = "Direção da ordenação (asc ou desc)", example = "asc") 
        @RequestParam(value = "direction", defaultValue = "asc") String direction);

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

    @Operation(summary = "Retorna a média de avaliações de uma ONG específica", 
        responses = {
            @ApiResponse(responseCode = "200", description = "Success", 
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = Double.class))),
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    ResponseEntity<Double> media(
        @Parameter(description = "ID da ONG para cálculo da média de avaliações") 
        @PathVariable("id") Long id);

    @Operation(summary = "Retorna a média de avaliações de todas as ONGs", 
        responses = {
            @ApiResponse(responseCode = "200", description = "Success", 
                content = @Content(mediaType = "application/json", 
                schema = @Schema(implementation = Map.class))),
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    ResponseEntity<List<Map<String, Object>>> listarTodasMediasDeAvaliacaoDasOngs();
}
