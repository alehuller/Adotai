package br.com.alevhvm.adotai.avaliacao.controller;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.alevhvm.adotai.avaliacao.dto.AvaliacaoDTO;
import br.com.alevhvm.adotai.avaliacao.service.AvaliacaoService;
import br.com.alevhvm.adotai.common.docs.AvaliacaoControllerDocs;
import br.com.alevhvm.adotai.common.util.MediaType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/avaliacoes")
public class AvaliacaoController implements AvaliacaoControllerDocs{
    
    private final AvaliacaoService avaliacaoService;

    @GetMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                    MediaType.APPLICATION_XML })
    public ResponseEntity<PagedModel<EntityModel<AvaliacaoDTO>>> listarTodasAvaliacoes(
                    @RequestParam(value = "page", defaultValue = "0") int page,
                    @RequestParam(value = "size", defaultValue = "10") int size,
                    @RequestParam(value = "direction", defaultValue = "asc") String direction) {
            
            var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

            Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "id"));
            return ResponseEntity.ok(avaliacaoService.findAll(pageable));
    }

    @GetMapping(value = "/{id}/ong", produces = { MediaType.APPLICATION_JSON,
                    MediaType.APPLICATION_YML,
                    MediaType.APPLICATION_XML })
    public ResponseEntity<PagedModel<EntityModel<AvaliacaoDTO>>> listarAvaliacoesDeUmaOng(
                    @PathVariable("id") Long id,
                    @RequestParam(value = "page", defaultValue = "0") int page,
                    @RequestParam(value = "size", defaultValue = "10") int size,
                    @RequestParam(value = "direction", defaultValue = "asc") String direction) {

            var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
            Pageable pageable = PageRequest.of(page, size, sortDirection, "id");

            PagedModel<EntityModel<AvaliacaoDTO>> pagedModel = avaliacaoService.findAllAvaliacoesByOngId(id,
                            pageable);

            return ResponseEntity.ok(pagedModel);
        }

    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                    MediaType.APPLICATION_XML })
    public ResponseEntity<AvaliacaoDTO> acharAvaliacaoPorId(@PathVariable(value = "id") Long id) {
        AvaliacaoDTO dto = avaliacaoService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                                        MediaType.APPLICATION_XML })
    public ResponseEntity<AvaliacaoDTO> registrarAvaliacao(@RequestBody @Valid AvaliacaoDTO avaliacaoDTO) {
        AvaliacaoDTO criado = avaliacaoService.create(avaliacaoDTO);
        URI location = linkTo(methodOn(AvaliacaoController.class).acharAvaliacaoPorId(criado.getKey())).toUri();
        return ResponseEntity.created(location).body(criado);
    }

    @GetMapping(value = "/{id}/ong/media", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                    MediaType.APPLICATION_XML })
    public ResponseEntity<Double> media(@PathVariable(value = "id") Long id) {
        Double media = avaliacaoService.calcularMedia(id);
        return ResponseEntity.ok(media);
    }

    @GetMapping(value = "/ongs/medias", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                    MediaType.APPLICATION_XML })
    public ResponseEntity<List<Map<String, Object>>> listarTodasMediasDeAvaliacaoDasOngs() {
            List<Map<String, Object>> medias = avaliacaoService.findAllAverage();
            return ResponseEntity.ok(medias);
    }
}
