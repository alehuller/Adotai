package br.com.alevh.sistema_adocao_pets.common.docs;

import java.util.Map;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.alevh.sistema_adocao_pets.adocao.dto.AdocaoDTO;
import br.com.alevh.sistema_adocao_pets.animal.dto.AnimalDTO;
import br.com.alevh.sistema_adocao_pets.ong.dto.OngDTO;
import br.com.alevh.sistema_adocao_pets.ong.dto.OngUpdateDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

public interface OngControllerDocs {
    @Operation(summary = "Retorna todas as ONGs", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OngDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    ResponseEntity<PagedModel<EntityModel<OngDTO>>> listarOngs(int page, int size, String direction);

    @Operation(summary = "Retorna a ONG de id especificado", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OngDTO.class)))),
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    OngDTO acharOngPorId(Long id);

    @Operation(summary = "Retorna a ong pelo nome de usuário pesquisado", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OngDTO.class)))),
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    OngDTO acharOngPorNomeUsuario(@PathVariable(value = "nomeUsuario") String nomeUsuario);

    @Operation(summary = "Retorna todas as adoções de uma ONG", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdocaoDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    ResponseEntity<PagedModel<EntityModel<AdocaoDTO>>> listarAdocoesPorOngId(Long id, int page, int size,
            String direction);

    @Operation(summary = "Retorna todas os Animais de uma ONG", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AnimalDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    ResponseEntity<PagedModel<EntityModel<AnimalDTO>>> listarAnimaisDeUmaOng(String nomeUsuario, int page, int size, String direction);

    @Operation(summary = "Registra uma ONG", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OngDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    OngDTO registrarOng(OngDTO ong);

    @Operation(summary = "Atualiza a ONG", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OngDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    OngDTO atualizarOng(String nomeUsuario, OngUpdateDTO ong);

    @Operation(summary = "Atualização parcial da ONG", responses = {
            @ApiResponse(responseCode = "200", description = "Success", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OngDTO.class)))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    ResponseEntity<OngDTO> atualizarParcialOng(String nomeUsuario, Map<String, Object> updates);

    @Operation(summary = "Apaga a ONG de nome especificado", responses = {
            @ApiResponse(responseCode = "204", description = "No Content", content = @Content),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal Error", content = @Content)
    })
    ResponseEntity<?> deletarOngPorNomeUsuario(String nomeUsuario);
}
