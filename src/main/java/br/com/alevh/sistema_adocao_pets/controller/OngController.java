package br.com.alevh.sistema_adocao_pets.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alevh.sistema_adocao_pets.data.vo.v1.OngVO;
import br.com.alevh.sistema_adocao_pets.model.Ong;
import br.com.alevh.sistema_adocao_pets.service.OngService;
import br.com.alevh.sistema_adocao_pets.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/ongs")
@Tag(name = "Ongs", description = "Endpoints para manipulação do registro das ONGS.")
public class OngController {

    private final OngService ongService;

    @GetMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML, MediaType.APPLICATION_XML})
    @Operation(summary = "Retorna todas as ONGS", responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content = {
                                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OngVO.class)))
                    }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
    })
    public ResponseEntity<PagedModel<EntityModel<OngVO>>> listarOngs(
                    @RequestParam(value = "page", defaultValue = "0") int page,
                    @RequestParam(value = "size", defaultValue = "10") int size,
                    @RequestParam(value = "direction", defaultValue = "asc") String direction) {

            var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "nome"));
            return ResponseEntity.ok(ongService.findAll(pageable));
    }

    @GetMapping(value = "/pesquisa", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML, MediaType.APPLICATION_XML})
    @Operation(summary = "Retorna a ONG de id especificado", responses = {
                   @ApiResponse(description = "Success", responseCode = "200", content = {
                                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Ong.class)))
                   }),
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
    })
    public Ong acharPorId(@RequestParam(value = "id") Long id) {
        return ongService.findById(id);
    }
}
