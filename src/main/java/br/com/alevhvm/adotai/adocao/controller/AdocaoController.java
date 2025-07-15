package br.com.alevhvm.adotai.adocao.controller;

import java.net.URI;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.alevhvm.adotai.common.docs.AdocaoControllerDocs;
import br.com.alevhvm.adotai.adocao.dto.AdocaoDTO;
import br.com.alevhvm.adotai.adocao.service.AdocaoService;
import br.com.alevhvm.adotai.common.util.MediaType;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/adocoes")
@Tag(name = "Adoções", description = "Endpoints para manipulação do registro de adoções.")
public class AdocaoController implements AdocaoControllerDocs{

        private final AdocaoService adocaoService;

        @GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML })
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
        public ResponseEntity<AdocaoDTO> acharAdocaoPorId(@PathVariable(value = "id") Long id) {
                return ResponseEntity.ok(adocaoService.findById(id));
        }

        @PostMapping(consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                                        MediaType.APPLICATION_XML })
        public ResponseEntity<AdocaoDTO> registrarAdocao(@RequestBody AdocaoDTO adocao) {
                AdocaoDTO criada = adocaoService.create(adocao);
                URI location = linkTo(methodOn(AdocaoController.class).acharAdocaoPorId(criada.getKey())).toUri();
                return ResponseEntity.created(location).body(criada);
        }

        @PutMapping(value = "/{id}", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                                        MediaType.APPLICATION_XML })
        public ResponseEntity<AdocaoDTO> atualizarAdocao(@PathVariable(value = "id") Long id, @RequestBody AdocaoDTO adocao) {
                AdocaoDTO atualizado = adocaoService.update(adocao, id);
                return ResponseEntity.ok(atualizado);
        }

        @PatchMapping(value = "/{id}", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                                        MediaType.APPLICATION_XML })
        public ResponseEntity<AdocaoDTO> atualizarParcialAdocao(@PathVariable(value = "id") Long id, @RequestBody Map<String, Object> updates) {
                AdocaoDTO adocaoAtualizado = adocaoService.partialUpdate(id, updates);
                return ResponseEntity.ok(adocaoAtualizado);
        }

        @DeleteMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML })
        public ResponseEntity<?> deletarPorId(@PathVariable(name = "id") Long id) {
                adocaoService.delete(id);
                return ResponseEntity.noContent().build();
        }
}