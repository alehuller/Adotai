package br.com.alevh.sistema_adocao_pets.controller;

import java.util.logging.Logger;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.alevh.sistema_adocao_pets.data.dto.v1.AnimalDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.UsuarioDTO;
import br.com.alevh.sistema_adocao_pets.service.AnimalService;
import br.com.alevh.sistema_adocao_pets.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/animais")
@Tag(name = "Animais", description = "Endpoints para manipulação do registro dos animais.")
public class AnimalController {

    private Logger logger = Logger.getLogger(AnimalService.class.getName());

    private final AnimalService animalService;

    @GetMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML, MediaType.APPLICATION_XML })
    @Operation(summary = "Retorna todos os animais", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AnimalDTO.class)))
            }),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
    })
    public ResponseEntity<PagedModel<EntityModel<AnimalDTO>>> listarAnimais(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "direction", defaultValue = "asc") String direction) {

        var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "nome"));
        return ResponseEntity.ok(animalService.findAll(pageable));
    }

    @GetMapping(value = "/pesquisa", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
            MediaType.APPLICATION_XML })
    @Operation(summary = "Retorna o animal de id especificado", responses = {
            @ApiResponse(description = "Success", responseCode = "200", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AnimalDTO.class))) 
            }),
            @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
    })
    public AnimalDTO acharPorId(@RequestParam(value = "id") Long id) {
        logger.info(String.format("Consultando animal de id $d", id));
        return animalService.findById(id);
    }
}
