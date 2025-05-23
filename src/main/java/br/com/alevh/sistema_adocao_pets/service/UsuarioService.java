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

import br.com.alevh.sistema_adocao_pets.mapper.DozerMapper;
import br.com.alevh.sistema_adocao_pets.controller.UsuarioController;
import br.com.alevh.sistema_adocao_pets.data.vo.v1.UsuarioVO;
import br.com.alevh.sistema_adocao_pets.exceptions.RequiredObjectIsNullException;
import br.com.alevh.sistema_adocao_pets.exceptions.ResourceNotFoundException;
import br.com.alevh.sistema_adocao_pets.model.Usuario;
import br.com.alevh.sistema_adocao_pets.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    private final PagedResourcesAssembler<UsuarioVO> assembler;

    public PagedModel<EntityModel<UsuarioVO>> findAll(Pageable pageable) {

        var usuarioPage = usuarioRepository.findAll(pageable);

        var usuarioVosPage = usuarioPage.map(u -> DozerMapper.parseObject(u, UsuarioVO.class));
        usuarioVosPage.map(u -> u.add(linkTo(methodOn(UsuarioController.class).acharPorId(u.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(UsuarioController.class).listarUsuarios(pageable.getPageNumber(),
                pageable.getPageSize(), "asc")).withSelfRel();
        return assembler.toModel(usuarioVosPage, link);
        // return usuarioRepository.findAll();
    }

    public UsuarioVO create(UsuarioVO usuario) {
        
        if(usuario == null) throw new RequiredObjectIsNullException();
        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        var entity = DozerMapper.parseObject(usuario, Usuario.class);
        var vo = DozerMapper.parseObject(usuarioRepository.save(entity), UsuarioVO.class);
        vo.add(linkTo(methodOn(UsuarioController.class).acharPorId(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }

    public UsuarioVO update(UsuarioVO usuario) { 
        
        if(usuario == null) throw new RequiredObjectIsNullException();

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        
        var entity = usuarioRepository.findById(usuario.getKey()).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        entity.setNome(usuario.getNome());
        entity.setEmail(usuario.getEmail());
        entity.setSenha(usuario.getSenha());
        entity.setCell(usuario.getCell());
        entity.setCpf(usuario.getCpf());

        var vo = DozerMapper.parseObject(usuarioRepository.save(entity), UsuarioVO.class);
        vo.add(linkTo(methodOn(UsuarioController.class).acharPorId(vo.getKey())).withSelfRel());
        return vo;
    }

    public UsuarioVO findById(Long id) {

        var entity = usuarioRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        var vo = DozerMapper.parseObject(entity, UsuarioVO.class);
        vo.add(linkTo(methodOn(UsuarioController.class).acharPorId(id)).withSelfRel());
        return vo;
    }
}
