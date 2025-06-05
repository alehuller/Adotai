package br.com.alevh.sistema_adocao_pets.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.alevh.sistema_adocao_pets.data.dto.v1.AdocaoDTO;
import br.com.alevh.sistema_adocao_pets.service.AdocaoService;
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
@RequestMapping("api/v1/adocoes")
@Tag(name = "Adoções", description = "Endpoints para manipulação do registro de adoções.")
public class AdocaoController {

        private final AdocaoService adocaoService;

        @GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
        @Operation(summary = "Retorna todas as adoções", responses = {
                        @ApiResponse(description = "Success", responseCode = "200", content = {
                                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdocaoDTO.class)))
                        }),
                        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                        @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
        })
        public ResponseEntity<PagedModel<EntityModel<AdocaoDTO>>> listarAdocoes(
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size,
                        @RequestParam(value = "direction", defaultValue = "asc") String direction) {

                var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

                Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "dataAdocao"));
                return ResponseEntity.ok(adocaoService.findAll(pageable));
        }

        @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML })
        @Operation(summary = "Retorna a adoção de id especificado", responses = {
                        @ApiResponse(description = "Success", responseCode = "200", content = {
                                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdocaoDTO.class)))
                        }),
                        @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                        @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
        })
        public AdocaoDTO acharAdocaoPorId(@PathVariable(value = "id") Long id) {
                return adocaoService.findById(id);
        }

        @PostMapping(value = "/registro", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                                        MediaType.APPLICATION_XML })
        @Operation(summary = "Registra uma adoção", responses = {
                        @ApiResponse(description = "Success", responseCode = "200", content = {
                                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdocaoDTO.class))) 
                        }),
                        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                        @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
        })
        public AdocaoDTO registrarAdocao(@RequestBody AdocaoDTO adocao) {
                return adocaoService.create(adocao);
        }

        @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML })
        @Operation(summary = "Apaga a adoção de id especificado", responses = {
                        @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                        @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
        })
        public ResponseEntity<?> deletarPorId(@PathVariable(name = "id") Long id) {
                adocaoService.delete(id);
                return ResponseEntity.noContent().build();
        }

        @PutMapping(value = "/{id}", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                                        MediaType.APPLICATION_XML })
        @Operation(summary = "Atualiza a adoção", responses = {
                        @ApiResponse(description = "Success", responseCode = "200", content = {
                                        @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AdocaoDTO.class)))
                        }),
                        @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                        @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                        @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                        @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
        })
        public AdocaoDTO atualizarAdocao(@PathVariable(value = "id") Long id, @RequestBody AdocaoDTO adocao) {
                return adocaoService.update(adocao, id);
        }
}