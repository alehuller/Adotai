package br.com.alevh.sistema_adocao_pets.service;

import br.com.alevh.sistema_adocao_pets.config.TokenService;
import br.com.alevh.sistema_adocao_pets.controller.AdocaoController;
import br.com.alevh.sistema_adocao_pets.controller.UsuarioController;
import br.com.alevh.sistema_adocao_pets.data.dto.security.LoginDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.security.TokenDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.security.RegistroDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.AdocaoDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.UsuarioDTO;
import br.com.alevh.sistema_adocao_pets.exceptions.RequiredObjectIsNullException;
import br.com.alevh.sistema_adocao_pets.exceptions.ResourceNotFoundException;
import br.com.alevh.sistema_adocao_pets.mapper.DozerMapper;
import br.com.alevh.sistema_adocao_pets.model.Adocao;
import br.com.alevh.sistema_adocao_pets.model.Usuario;
import br.com.alevh.sistema_adocao_pets.repository.AdocaoRepository;
import br.com.alevh.sistema_adocao_pets.repository.UsuarioRepository;
import br.com.alevh.sistema_adocao_pets.util.UsuarioRole;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    private final AdocaoRepository adocaoRepository;

    private final PasswordEncoder passwordEncoder;

    private final PagedResourcesAssembler<UsuarioDTO> assembler;

    private final AuthenticationManager authenticationManager;

    private final TokenService tokenService;

    private final PagedResourcesAssembler<AdocaoDTO> adocaoDtoAssembler;

    public PagedModel<EntityModel<UsuarioDTO>> findAll(Pageable pageable) {

        Page<Usuario> usuarioPage = usuarioRepository.findAll(pageable);

        Page<UsuarioDTO> usuarioDtosPage = usuarioPage.map(u -> DozerMapper.parseObject(u, UsuarioDTO.class));
        usuarioDtosPage
                .map(u -> u.add(linkTo(methodOn(UsuarioController.class).acharUsuarioPorId(u.getKey())).withSelfRel()));

        Link link = linkTo(methodOn(UsuarioController.class).listarUsuarios(pageable.getPageNumber(),
                pageable.getPageSize(), "asc")).withSelfRel();
        return assembler.toModel(usuarioDtosPage, link);
    }

    public UsuarioDTO create(RegistroDTO registroDTO) {
        if (registroDTO == null) {
            throw new RequiredObjectIsNullException("JSON vazio.");
        }
        // se encontrar o usuario no bd retorna badrequest
        if (existsUsuarioWithEmail(registroDTO.getEmail())) {
            throw new IllegalStateException("E-mail já está em uso.");
        }
        if (existsUsuarioWithCpf(registroDTO.getCpf().getCpf())) {
            throw new IllegalStateException("CPF já está em uso.");
        }
        if (existsUsuarioWithCell(registroDTO.getCell())) {
            throw new IllegalStateException("Cell já está em uso.");
        }
        Usuario entity = DozerMapper.parseObject(registroDTO, Usuario.class);
        entity.setCpf(registroDTO.getCpf().getCpf());
        entity.setSenha(passwordEncoder.encode(registroDTO.getPassword()));
        entity.setRole(UsuarioRole.USER);
        UsuarioDTO dto = DozerMapper.parseObject(usuarioRepository.save(entity), UsuarioDTO.class);
        dto.add(
                linkTo(
                        methodOn(UsuarioController.class).acharUsuarioPorId(dto.getKey())).withSelfRel());
        return dto;
    }

    public TokenDTO logar(LoginDTO data) {

        // credenciais do spring security
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.identifier(), data.password());

        // autentica de forma milagrosa as credenciais
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((Usuario) auth.getPrincipal());

        return new TokenDTO(token);
    }

    public void delete(Long id) {
        usuarioRepository.deleteById(id);
    }

    public UsuarioDTO update(UsuarioDTO usuario, Long id) {

        // if(usuario == null) throw new RequiredObjectIsNullException();

        usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));

        Usuario entity = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        entity.setNome(usuario.getNome());
        entity.setNomeUsuario(usuario.getNomeUsuario());
        entity.setFotoPerfil(usuario.getFotoPerfil());
        entity.setEmail(usuario.getEmail());
        entity.setSenha(usuario.getSenha());
        entity.setCell(usuario.getCell());
        entity.setCpf(usuario.getCpf().getCpf());

        UsuarioDTO dto = DozerMapper.parseObject(usuarioRepository.save(entity), UsuarioDTO.class);
        dto.add(linkTo(methodOn(UsuarioController.class).acharUsuarioPorId(dto.getKey())).withSelfRel());
        return dto;
    }

    public UsuarioDTO findById(Long id) {

        Usuario entity = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        UsuarioDTO dto = DozerMapper.parseObject(entity, UsuarioDTO.class);
        dto.add(linkTo(methodOn(UsuarioController.class).acharUsuarioPorId(id)).withSelfRel());
        return dto;
    }

    public UsuarioDTO findByNomeUsuario(String nomeUsuario) {

        Usuario entity = usuarioRepository.findByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        UsuarioDTO dto = DozerMapper.parseObject(entity, UsuarioDTO.class);
        dto.add(linkTo(methodOn(UsuarioController.class).acharUsuarioPorNomeUsuario(nomeUsuario)).withSelfRel());
        return dto;
    }

    public PagedModel<EntityModel<AdocaoDTO>> findAllAdocoesByUsuarioId(Long idUsuario, Pageable pageable) {

        Page<Adocao> adocaoPage = adocaoRepository.findAdocoesByUsuarioId(idUsuario, pageable);

        Page<AdocaoDTO> adocaoDtoPage = adocaoPage.map(a -> DozerMapper.parseObject(a, AdocaoDTO.class));

        adocaoDtoPage = adocaoDtoPage.map(
                dto -> dto.add(linkTo(methodOn(AdocaoController.class).acharAdocaoPorId(dto.getKey())).withSelfRel()));

        Link selfLink = linkTo(methodOn(UsuarioController.class)
                .listarAdocoesPorUsuarioId(idUsuario, pageable.getPageNumber(), pageable.getPageSize(), "asc"))
                .withSelfRel();

        return adocaoDtoAssembler.toModel(adocaoDtoPage, selfLink);
    }

    public UsuarioDTO partialUpdate(Long id, Map<String, Object> updates) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado."));

        ObjectMapper mapper = new ObjectMapper();
        updates.forEach((campo, valor) -> {
            Field field = ReflectionUtils.findField(Usuario.class, campo);
            if (field != null) {
                field.setAccessible(true);
                ReflectionUtils.setField(field, usuario, mapper.convertValue(valor, field.getType()));
            }
        });

        usuarioRepository.save(usuario);
        return DozerMapper.parseObject(usuario, UsuarioDTO.class);
    }

    public boolean existsUsuarioWithEmail(String email) {
        return usuarioRepository.findByEmail(email).isPresent();
    }

    public boolean existsUsuarioWithCpf(String cpf) {
        return usuarioRepository.findByCpf(cpf).isPresent();
    }

    public boolean existsUsuarioWithCell(String cell) {
        return usuarioRepository.findByCell(cell).isPresent();
    }
}
