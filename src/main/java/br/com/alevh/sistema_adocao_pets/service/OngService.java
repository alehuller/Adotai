package br.com.alevh.sistema_adocao_pets.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import br.com.alevh.sistema_adocao_pets.controller.OngController;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.OngDTO;
import br.com.alevh.sistema_adocao_pets.exceptions.RequiredObjectIsNullException;
import br.com.alevh.sistema_adocao_pets.exceptions.ResourceNotFoundException;
import br.com.alevh.sistema_adocao_pets.mapper.DozerMapper;
import br.com.alevh.sistema_adocao_pets.model.Ong;
import br.com.alevh.sistema_adocao_pets.repository.OngRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OngService {

    private final OngRepository ongRepository;

    private final PasswordEncoder passwordEncoder;

    private final PagedResourcesAssembler<OngDTO> assembler;

    public PagedModel<EntityModel<OngDTO>> findAll(Pageable pageable) {
        
        Page<Ong> ongPage = ongRepository.findAll(pageable);

        Page<OngDTO> ongDtosPage = ongPage.map(o -> DozerMapper.parseObject(o, OngDTO.class));
        ongDtosPage.map(o -> o.add(linkTo(methodOn(OngController.class).acharOngPorId(o.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(OngController.class).listarOngs(pageable.getPageNumber(),
                pageable.getPageSize(), "asc")).withSelfRel();
        return assembler.toModel(ongDtosPage, link);
    }

    public OngDTO findById(Long id) {
        
        Ong entity = ongRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ong não encontrado."));

        OngDTO dto = DozerMapper.parseObject(entity, OngDTO.class);
        dto.add(linkTo(methodOn(OngController.class).acharOngPorId(id)).withSelfRel());
        return dto;

    }

    public OngDTO create(OngDTO ong) {
        
        if(ong == null) throw new RequiredObjectIsNullException();
        ong.setSenha(passwordEncoder.encode(ong.getSenha()));
        Ong entity = DozerMapper.parseObject(ong, Ong.class);
        OngDTO dto = DozerMapper.parseObject(ongRepository.save(entity), OngDTO.class);
        dto.add(linkTo(methodOn(OngController.class).acharOngPorId(dto.getKey())).withSelfRel());
        return dto;
    }

    public OngDTO update(OngDTO ong) {
        
        if(ong == null) throw new RequiredObjectIsNullException();

        ong.setSenha(passwordEncoder.encode(ong.getSenha()));
        
        Ong entity = ongRepository.findById(ong.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("Ong não encontrado."));

        entity.setNome(ong.getNome());
        entity.setEmail(ong.getEmail());
        entity.setSenha(ong.getSenha());
        entity.setEndereco(ong.getEndereco());
        entity.setTelefone(ong.getTelefone());
        entity.setCnpj(ong.getCnpj());
        entity.setResponsavel(ong.getResponsavel());

        OngDTO dto = DozerMapper.parseObject(ongRepository.save(entity), OngDTO.class);
        dto.add(linkTo(methodOn(OngController.class).acharOngPorId(dto.getKey())).withSelfRel());
        return dto;
    }

    public void delete(Long id) {
        ongRepository.deleteById(id);
    }
}
