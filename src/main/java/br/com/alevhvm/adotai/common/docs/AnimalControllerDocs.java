package br.com.alevhvm.adotai.common.docs;

import java.util.Map;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.alevhvm.adotai.animal.dto.AnimalDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;

public interface AnimalControllerDocs {
    @Operation(summary = "Retorna todos os animais", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AnimalDTO.class)))
            }),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
    })
    ResponseEntity<PagedModel<EntityModel<AnimalDTO>>> listarAnimais(
            @Parameter(description = "Número da página (padrão 0)") int page,
            @Parameter(description = "Tamanho da página (padrão 10)") int size,
            @Parameter(description = "Direção da ordenação: asc ou desc (padrão asc)") String direction);


    @Operation(summary = "Retorna o animal de id especificado", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AnimalDTO.class)))
            }),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
    })
    ResponseEntity<AnimalDTO> acharAnimalPorId(
            @Parameter(description = "ID do animal") 
            @PathVariable Long id);

    @Operation(summary = "Retorna o animal de nome especificado", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AnimalDTO.class)))
            }),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
    })
    ResponseEntity<AnimalDTO> acharAnimalPorNome(
            @Parameter(description = "Nome do animal") 
            @PathVariable String nome);

    @Operation(summary = "Registra um animal", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AnimalDTO.class)))
            }),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
    })
    ResponseEntity<AnimalDTO> registrarAnimal(
            @Parameter(description = "Dados do animal a ser registrado") 
            @RequestBody @Valid AnimalDTO animal);

    @Operation(summary = "Atualiza o animal", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AnimalDTO.class)))
            }),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
    })
    ResponseEntity<AnimalDTO> atualizarAnimal(
            @Parameter(description = "Nome do animal a ser atualizado") 
            @PathVariable String nome,
            @Parameter(description = "Dados atualizados do animal") 
            @RequestBody @Valid AnimalDTO animal);

    @Operation(summary = "Atualização parcial de animal", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AnimalDTO.class)))
            }),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
    })
    ResponseEntity<AnimalDTO> atualizarParcialAnimal(
            @Parameter(description = "Nome do animal a ser atualizado parcialmente") 
            @PathVariable String nome,
            @Parameter(description = "Campos e valores para atualização parcial") 
            @RequestBody Map<String, Object> updates);

    @Operation(summary = "Apaga o animal de nome especificado", responses = {
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
    })
    ResponseEntity<?> deletarPorNome(
            @Parameter(description = "Nome do animal a ser deletado") 
            @PathVariable String nome);
}
