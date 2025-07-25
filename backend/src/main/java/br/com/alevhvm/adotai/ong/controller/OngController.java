package br.com.alevhvm.adotai.ong.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alevhvm.adotai.common.docs.OngControllerDocs;
import br.com.alevhvm.adotai.adocao.dto.AdocaoDTO;
import br.com.alevhvm.adotai.animal.dto.AnimalDTO;
import br.com.alevhvm.adotai.ong.dto.OngDTO;
import br.com.alevhvm.adotai.ong.dto.OngFiltroDTO;
import br.com.alevhvm.adotai.ong.dto.OngUpdateDTO;
import br.com.alevhvm.adotai.animal.service.AnimalService;
import br.com.alevhvm.adotai.ong.service.OngService;
import br.com.alevhvm.adotai.common.util.MediaType;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.net.URI;
import java.util.Map;

import org.springframework.data.domain.Page;
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
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/ongs")
@Tag(name = "Ongs", description = "Endpoints para manipulação do registro das ONGS.")
public class OngController implements OngControllerDocs {

        private final OngService ongService;

        private final AnimalService animalService;

        @GetMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML, MediaType.APPLICATION_XML })
        public ResponseEntity<PagedModel<EntityModel<OngDTO>>> listarOngs(
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size,
                        @RequestParam(value = "direction", defaultValue = "asc") String direction) {

                var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

                Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "nome"));
                return ResponseEntity.ok(ongService.findAll(pageable));
        }

        @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML })
        public ResponseEntity<OngDTO> acharOngPorId(@PathVariable(value = "id") Long id) {
                OngDTO dto = ongService.findById(id);
                return ResponseEntity.ok(dto);
        }

        @GetMapping(value = "/nomeUsuario/{nomeUsuario}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML })
        public ResponseEntity<OngDTO> acharOngPorNomeUsuario(@PathVariable(value = "nomeUsuario") String nomeUsuario) {
                OngDTO dto = ongService.findByNomeUsuario(nomeUsuario);
                return ResponseEntity.ok(dto);
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

        @GetMapping(value = "/{nomeUsuario}/animais", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML, MediaType.APPLICATION_XML})
        public ResponseEntity<PagedModel<EntityModel<AnimalDTO>>> listarAnimaisDeUmaOng(
                        @PathVariable("nomeUsuario") String nomeUsuario,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size,
                        @RequestParam(value = "direction", defaultValue = "asc") String direction) {

                var sortDirection = "desc".equalsIgnoreCase(direction) ? Sort.Direction.DESC : Sort.Direction.ASC;
                Pageable pageable = PageRequest.of(page, size, sortDirection, "nome");

                PagedModel<EntityModel<AnimalDTO>> pagedModel = animalService.findAllByOngNome(nomeUsuario, pageable);
                return ResponseEntity.ok(pagedModel);
        }

        @PostMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML }, consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                                        MediaType.APPLICATION_XML })
        public ResponseEntity<OngDTO> registrarOng(@RequestBody @Valid OngDTO ong) {
                OngDTO criado = ongService.create(ong);
                URI location = linkTo(methodOn(OngController.class).acharOngPorId(criado.getKey())).toUri();
                return ResponseEntity.created(location).body(criado);
        }

        @PostMapping(value = "/filtro", produces = MediaType.APPLICATION_JSON)
        public ResponseEntity<Page<OngDTO>> filtrarOngs(
                        @RequestBody OngFiltroDTO filtro,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size,
                        @RequestParam(value = "direction", defaultValue = "asc") String direction) {

                Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC
                                : Sort.Direction.ASC;
                Pageable pageable = PageRequest.of(page, size, sortDirection, "nome");
                Page<OngDTO> resultados = ongService.filtrarOngs(filtro, pageable);
                return ResponseEntity.ok(resultados);
        }

        @PreAuthorize("#nomeUsuario == principal.nomeUsuario or hasRole('ADMIN')")
        @PutMapping(value = "/{nomeUsuario}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML }, consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                                        MediaType.APPLICATION_XML })
        public ResponseEntity<OngDTO> atualizarOng(@PathVariable(value = "nomeUsuario") String nomeUsuario, @RequestBody @Valid OngUpdateDTO ong) {
                OngDTO dto = ongService.update(ong, nomeUsuario);
                return ResponseEntity.ok(dto);
        }

        @PreAuthorize("#nomeUsuario == principal.nomeUsuario or hasRole('ADMIN')")
        @PatchMapping(value = "/{nomeUsuario}", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                                        MediaType.APPLICATION_XML })
        public ResponseEntity<OngDTO> atualizarParcialOng(@PathVariable(value = "nomeUsuario") String nomeUsuario,
                        @RequestBody Map<String, Object> updates) {
                OngDTO ongAtualizada = ongService.partialUpdate(nomeUsuario, updates);
                return ResponseEntity.ok(ongAtualizada);
        }

        @PreAuthorize("#nomeUsuario == principal.nomeUsuario or hasRole('ADMIN')")
        @DeleteMapping(value = "/{nomeUsuario}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML })
        public ResponseEntity<?> deletarOngPorNomeUsuario(@PathVariable(name = "nomeUsuario") String nomeUsuario) {
                ongService.delete(nomeUsuario);
                return ResponseEntity.noContent().build();
        }
}