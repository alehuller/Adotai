package br.com.alevh.sistema_adocao_pets.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import br.com.alevh.sistema_adocao_pets.controller.AnimalController;
import br.com.alevh.sistema_adocao_pets.controller.UsuarioController;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.AnimalDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.UsuarioDTO;
import br.com.alevh.sistema_adocao_pets.exceptions.ResourceNotFoundException;
import br.com.alevh.sistema_adocao_pets.mapper.DozerMapper;
import br.com.alevh.sistema_adocao_pets.model.Animal;
import br.com.alevh.sistema_adocao_pets.model.Usuario;
import br.com.alevh.sistema_adocao_pets.repository.AnimalRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnimalService {

    private final AnimalRepository animalRepository;

    private final PagedResourcesAssembler<AnimalDTO> assembler;

    public PagedModel<EntityModel<AnimalDTO>> findAll(Pageable pageable) {

        Page<Animal> animalPage = animalRepository.findAll(pageable);

        Page<AnimalDTO> animalDtosPage = animalPage.map(a -> DozerMapper.parseObject(a, AnimalDTO.class));
        animalDtosPage.map(a -> a.add(linkTo(methodOn(AnimalController.class).acharPorId(a.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(AnimalController.class).listarAnimais(pageable.getPageNumber(),
                pageable.getPageSize(), "asc")).withSelfRel();
        return assembler.toModel(animalDtosPage, link);
        // return usuarioRepository.findAll();
    }

    public AnimalDTO findById(Long id) {

        Animal entity = animalRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Animal não encontrado."));

        AnimalDTO dto = DozerMapper.parseObject(entity, AnimalDTO.class);
        dto.add(linkTo(methodOn(AnimalController.class).acharPorId(id)).withSelfRel());
        return dto;
    }
}
