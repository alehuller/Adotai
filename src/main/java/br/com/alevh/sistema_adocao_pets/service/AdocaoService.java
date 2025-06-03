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

import br.com.alevh.sistema_adocao_pets.controller.AdocaoController;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.AdocaoDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.AnimalDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.OngDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.UsuarioDTO;
import br.com.alevh.sistema_adocao_pets.exceptions.RequiredObjectIsNullException;
import br.com.alevh.sistema_adocao_pets.exceptions.ResourceNotFoundException;
import br.com.alevh.sistema_adocao_pets.mapper.DozerMapper;
import br.com.alevh.sistema_adocao_pets.model.Adocao;
import br.com.alevh.sistema_adocao_pets.model.Animal;
import br.com.alevh.sistema_adocao_pets.model.Ong;
import br.com.alevh.sistema_adocao_pets.model.Usuario;
import br.com.alevh.sistema_adocao_pets.repository.AdocaoRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdocaoService {

    private final AdocaoRepository adocaoRepository;

    private final UsuarioService usuarioService;

    private final AnimalService animalService;

    private final OngService ongService;

    private final PagedResourcesAssembler<AdocaoDTO> assembler;

    public PagedModel<EntityModel<AdocaoDTO>> findAll(Pageable pageable) {

        Page<Adocao> adocaoPage = adocaoRepository.findAll(pageable);

        Page<AdocaoDTO> adocaoDtosPage = adocaoPage.map(a -> DozerMapper.parseObject(a, AdocaoDTO.class));
        adocaoDtosPage.map(a -> a.add(linkTo(methodOn(AdocaoController.class).acharPorId(a.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(AdocaoController.class).listarAdocoes(pageable.getPageNumber(),
                pageable.getPageSize(), "asc")).withSelfRel();
        return assembler.toModel(adocaoDtosPage, link);
    }

    public AdocaoDTO findById(Long id) {
        
        Adocao entity = adocaoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Adoção não encontrada."));

        AdocaoDTO dto = DozerMapper.parseObject(entity, AdocaoDTO.class);
        dto.add(linkTo(methodOn(AdocaoController.class).acharPorId(id)).withSelfRel());
        return dto;
    }

    public AdocaoDTO create(AdocaoDTO adocao) {
        
        if (adocao == null) throw new RequiredObjectIsNullException();
        Adocao entity = DozerMapper.parseObject(adocao, Adocao.class);
        AdocaoDTO dto = DozerMapper.parseObject(adocaoRepository.save(entity), AdocaoDTO.class);
        dto.add(linkTo(methodOn(AdocaoController.class).acharPorId(dto.getKey())).withSelfRel());
        return dto;
    }

    public void delete(Long id) {
        adocaoRepository.deleteById(id);
    }

    public AdocaoDTO update(AdocaoDTO adocao) {
        
        if(adocao == null) throw new RequiredObjectIsNullException();

        Adocao entity = adocaoRepository.findById(adocao.getKey()).orElseThrow(() -> new ResourceNotFoundException("Adoção não encontrada."));

        UsuarioDTO usuarioDTO = usuarioService.findById(adocao.getIdUsuario());
        Usuario usuario = DozerMapper.parseObject(usuarioDTO, Usuario.class);

        AnimalDTO animalDTO = animalService.findById(adocao.getIdAnimal());
        Animal animal = DozerMapper.parseObject(animalDTO, Animal.class);

        OngDTO ongDTO = ongService.findById(animal.getOng().getIdOng());
        Ong ong = DozerMapper.parseObject(ongDTO, Ong.class);

        entity.setDataAdocao(adocao.getDataAdocao());
        entity.setStatus(adocao.getStatus());
        entity.setUsuario(usuario);
        animal.setOng(ong);
        entity.setAnimal(animal);

        AdocaoDTO dto = DozerMapper.parseObject(adocaoRepository.save(entity), AdocaoDTO.class);
        dto.add(linkTo(methodOn(AdocaoController.class).acharPorId(dto.getKey())).withSelfRel());
        return dto;
    }

}
