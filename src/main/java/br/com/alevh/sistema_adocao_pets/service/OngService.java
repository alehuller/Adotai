package br.com.alevh.sistema_adocao_pets.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import br.com.alevh.sistema_adocao_pets.controller.OngController;
import br.com.alevh.sistema_adocao_pets.data.vo.v1.OngVO;
import br.com.alevh.sistema_adocao_pets.exceptions.ResourceNotFoundException;
import br.com.alevh.sistema_adocao_pets.mapper.DozerMapper;
import br.com.alevh.sistema_adocao_pets.model.Ong;
import br.com.alevh.sistema_adocao_pets.repository.OngRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OngService {

    private final OngRepository ongRepository;

    private final PagedResourcesAssembler<OngVO> assembler;

    public PagedModel<EntityModel<OngVO>> findAll(Pageable pageable) {
        
        var ongPage = ongRepository.findAll(pageable);

        var ongVosPage = ongPage.map(o -> DozerMapper.parseObject(o, OngVO.class));
        ongVosPage.map(o -> o.add(linkTo(methodOn(OngController.class).acharPorId(o.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(OngController.class).listarOngs(pageable.getPageNumber(),
                pageable.getPageSize(), "asc")).withSelfRel();
        return assembler.toModel(ongVosPage, link);
    }

    public Ong findById(Long id) {
        return ongRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(String.format("Usuário com id %d não encontrado!", id)));
    }

}
