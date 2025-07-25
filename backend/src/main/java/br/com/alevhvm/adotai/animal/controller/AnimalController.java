package br.com.alevhvm.adotai.animal.controller;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import br.com.alevhvm.adotai.common.docs.AnimalControllerDocs;
import br.com.alevhvm.adotai.animal.dto.AnimalDTO;
import br.com.alevhvm.adotai.animal.dto.AnimalFiltroDTO;
import br.com.alevhvm.adotai.animal.service.AnimalService;
import br.com.alevhvm.adotai.common.util.MediaType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/animais")
@Tag(name = "Animais", description = "Endpoints para manipulação do registro dos animais.")
public class AnimalController implements AnimalControllerDocs {

        private final AnimalService animalService;

        @GetMapping(produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML, MediaType.APPLICATION_XML })
        public ResponseEntity<PagedModel<EntityModel<AnimalDTO>>> listarAnimais(
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size,
                        @RequestParam(value = "direction", defaultValue = "asc") String direction) {

                var sortDirection = "desc".equalsIgnoreCase(direction) ? Direction.DESC : Direction.ASC;

                Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, "nome"));
                return ResponseEntity.ok(animalService.findAll(pageable));
        }

        @SecurityRequirement(name = "bearerAuth")
        @GetMapping(value = "/{id}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML })
        public ResponseEntity<AnimalDTO> acharAnimalPorId(@PathVariable(value = "id") Long id) {
                return ResponseEntity.ok(animalService.findById(id));
        }

        @GetMapping(value = "/nome/{nome}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML })
        public ResponseEntity<AnimalDTO> acharAnimalPorNome(@PathVariable(value = "nome") String nome) {
                AnimalDTO dto = animalService.findByNome(nome);
                return ResponseEntity.ok(dto);
        }

        @PostMapping(value = "/filtro", produces = MediaType.APPLICATION_JSON)
        public ResponseEntity<Page<AnimalDTO>> filtrarAnimais(
                        @RequestBody AnimalFiltroDTO filtro,
                        @RequestParam(value = "page", defaultValue = "0") int page,
                        @RequestParam(value = "size", defaultValue = "10") int size,
                        @RequestParam(value = "direction", defaultValue = "asc") String direction) {

                Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? Sort.Direction.DESC
                                : Sort.Direction.ASC;
                Pageable pageable = PageRequest.of(page, size, sortDirection, "nome");

                Page<AnimalDTO> resultados = animalService.filtrarAnimais(filtro, pageable);
                return ResponseEntity.ok(resultados);
        }

        @SecurityRequirement(name = "bearerAuth")
        @PostMapping(consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                                        MediaType.APPLICATION_XML })
        public ResponseEntity<AnimalDTO> registrarAnimal(@RequestBody AnimalDTO animal) {
                AnimalDTO criado = animalService.create(animal);
                URI location = linkTo(methodOn(AnimalController.class).acharAnimalPorId(criado.getKey())).toUri();
                return ResponseEntity.created(location).body(criado);
        }

        @SecurityRequirement(name = "bearerAuth")
        @PutMapping(value = "/{nome}", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                                        MediaType.APPLICATION_XML })
        public ResponseEntity<AnimalDTO> atualizarAnimal(@PathVariable(value = "nome") String nome, @RequestBody AnimalDTO animal) {
                AnimalDTO dto = animalService.update(animal, nome);
                return ResponseEntity.ok(dto);
        }

        @SecurityRequirement(name = "bearerAuth")
        @PatchMapping(value = "/{nome}", consumes = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML }, produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                                        MediaType.APPLICATION_XML })
        public ResponseEntity<AnimalDTO> atualizarParcialAnimal(@PathVariable(value = "nome") String nome,
                        @RequestBody Map<String, Object> updates) {
                AnimalDTO animalAtualizado = animalService.partialUpdate(nome, updates);
                return ResponseEntity.ok(animalAtualizado);
        }

        @SecurityRequirement(name = "bearerAuth")
        @DeleteMapping(value = "/{nome}", produces = { MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML,
                        MediaType.APPLICATION_XML })
        public ResponseEntity<?> deletarPorNome(@PathVariable(name = "nome") String nome) {
                animalService.delete(nome);
                return ResponseEntity.noContent().build();
        }
}