package br.com.alevh.sistema_adocao_pets.service;

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
import br.com.alevh.sistema_adocao_pets.data.vo.v1.OngVO;
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

    private final PagedResourcesAssembler<OngVO> assembler;

    public PagedModel<EntityModel<OngVO>> findAll(Pageable pageable) {
        
        var ongPage = ongRepository.findAll(pageable);

        var ongVosPage = ongPage.map(o -> DozerMapper.parseObject(o, OngVO.class));
        ongVosPage.map(o -> o.add(linkTo(methodOn(OngController.class).acharOngPorId(o.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(OngController.class).listarOngs(pageable.getPageNumber(),
                pageable.getPageSize(), "asc")).withSelfRel();
        return assembler.toModel(ongVosPage, link);
    }

    public OngVO findById(Long id) {
        
        var entity = ongRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ong não encontrado."));

        var vo = DozerMapper.parseObject(entity, OngVO.class);
        vo.add(linkTo(methodOn(OngController.class).acharOngPorId(id)).withSelfRel());
        return vo;

    }

    public OngVO create(OngVO ong) {
        
        if(ong == null) throw new RequiredObjectIsNullException();
        ong.setSenha(passwordEncoder.encode(ong.getSenha()));
        var entity = DozerMapper.parseObject(ong, Ong.class);
        var vo = DozerMapper.parseObject(ongRepository.save(entity), OngVO.class);
        vo.add(linkTo(methodOn(OngController.class).acharOngPorId(vo.getKey())).withSelfRel());
        return vo;
    }

    public OngVO update(OngVO ong) {
        
        if(ong == null) throw new RequiredObjectIsNullException();

        ong.setSenha(passwordEncoder.encode(ong.getSenha()));
        
        var entity = ongRepository.findById(ong.getKey())
                .orElseThrow(() -> new ResourceNotFoundException("Ong não encontrado."));

        entity.setNome(ong.getNome());
        entity.setEmail(ong.getEmail());
        entity.setSenha(ong.getSenha());
        entity.setEndereco(ong.getEndereco());
        entity.setTelefone(ong.getTelefone());
        entity.setCnpj(ong.getCnpj());
        entity.setResponsavel(ong.getResponsavel());

        var vo = DozerMapper.parseObject(ongRepository.save(entity), OngVO.class);
        vo.add(linkTo(methodOn(OngController.class).acharOngPorId(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        ongRepository.deleteById(id);
    }
}
