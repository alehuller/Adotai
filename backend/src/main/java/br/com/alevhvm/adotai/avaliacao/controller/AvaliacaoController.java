package br.com.alevhvm.adotai.avaliacao.controller;

import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
