package br.com.alevh.sistema_adocao_pets.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alevh.sistema_adocao_pets.controller.docs.OngControllerDocs;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.AdocaoDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.OngDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.OngUpdateDTO;
import br.com.alevh.sistema_adocao_pets.service.OngService;
import br.com.alevh.sistema_adocao_pets.util.MediaType;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/ongs")
@Tag(name = "Ongs", description = "Endpoints para manipulação do registro das ONGS.")
public class OngController implements OngControllerDocs {

        private final OngService ongService;

        @GetMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML, MediaType.APPLICATION_XML })
        public ResponseEntity<PagedModel<EntityModel<OngDTO>>> listarOngs(
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size,
                        @RequestParam(value = "direction", defaultValue = "asc") String direction) {

                var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

                Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "nome"));
                return ResponseEntity.ok(ongService.findAll(pageable));
        }

        @GetMapping(value = "/id/{id}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML })
        public OngDTO acharOngPorId(@PathVariable(value = "id") Long id) {
                return ongService.findById(id);
        }

        @GetMapping(value = "/{nome_usuario}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML })
        public OngDTO acharOngPorNomeUsuario(@PathVariable(value = "nome_usuario") String nomeUsuario) {
                return ongService.findByNomeUsuario(nomeUsuario);
        }

        @PostMapping(value = "/signup", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML }, consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                                        MediaType.APPLICATION_XML })
        public OngDTO registrarOng(@RequestBody @Valid OngDTO ong) {
                return ongService.create(ong);
        }

        @PutMapping(value = "/{nomeUsuario}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML }, consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                                        MediaType.APPLICATION_XML })
        public OngDTO atualizarOng(@PathVariable(value = "nomeUsuario") String nomeUsuario, @RequestBody @Valid OngUpdateDTO ong) {
                return ongService.update(ong, nomeUsuario);
        }

        @DeleteMapping(value = "/{nomeUsuario}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML })
        public ResponseEntity<?> deletarOngPorNomeUsuario(@PathVariable(name = "nomeUsuario") String nomeUsuario) {
                ongService.delete(nomeUsuario);
                return ResponseEntity.noContent().build();
        }

        @GetMapping(value = "/{id}/adocoes", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML })
        public ResponseEntity<PagedModel<EntityModel<AdocaoDTO>>> listarAdocoesPorOngId(
                        @PathVariable("id") Long id,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size,
                        @RequestParam(value = "direction", defaultValue = "asc") String direction) {

                var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
                Pageable pageable = PageRequest.of(page, size, sortDirection, "idAdocao");

                PagedModel<EntityModel<AdocaoDTO>> pagedModel = ongService.findAllAdocoesByOngId(id,
                                pageable);

                return ResponseEntity.ok(pagedModel);
        }

        @PatchMapping(value = "/{nomeUsuario}", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                                        MediaType.APPLICATION_XML })
        public ResponseEntity<OngDTO> atualizarParcialOng(@PathVariable(value = "nomeUsuario") String nomeUsuario,
                        @RequestBody Map<String, Object> updates) {
                OngDTO ongAtualizada = ongService.partialUpdate(nomeUsuario, updates);
                return ResponseEntity.ok(ongAtualizada);
        }
}