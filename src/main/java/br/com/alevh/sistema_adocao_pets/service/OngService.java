package br.com.alevh.sistema_adocao_pets.service;

import br.com.alevh.sistema_adocao_pets.data.dto.security.LoginDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.security.TokenDTO;
import br.com.alevh.sistema_adocao_pets.exceptions.RequiredObjectIsNullException;
import br.com.alevh.sistema_adocao_pets.exceptions.ResourceNotFoundException;
import br.com.alevh.sistema_adocao_pets.model.LoginIdentityView;
import br.com.alevh.sistema_adocao_pets.util.Roles;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;

import br.com.alevh.sistema_adocao_pets.controller.AdocaoController;
import br.com.alevh.sistema_adocao_pets.controller.OngController;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.AdocaoDTO;
import br.com.alevh.sistema_adocao_pets.data.dto.v1.OngDTO;
import br.com.alevh.sistema_adocao_pets.mapper.DozerMapper;
import br.com.alevh.sistema_adocao_pets.model.Adocao;
import br.com.alevh.sistema_adocao_pets.model.Ong;
import br.com.alevh.sistema_adocao_pets.repository.AdocaoRepository;
import br.com.alevh.sistema_adocao_pets.repository.OngRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OngService {

    private final OngRepository ongRepository;

    private final AdocaoRepository adocaoRepository;

    private final PasswordEncoder passwordEncoder;

    private final PagedResourcesAssembler<OngDTO> assembler;

    private final PagedResourcesAssembler<AdocaoDTO> adocaoDtoAssembler;

    private final Validator validator;

    private final AuthenticationManager authenticationManager;

    private final br.com.alevh.sistema_adocao_pets.service.auth.TokenService tokenService;

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

    public OngDTO findByNomeUsuario(String nomeUsuario) {

        Ong entity = ongRepository.findByNomeUsuario(nomeUsuario)
                .orElseThrow(() -> new ResourceNotFoundException("Ong não encontrado."));

        OngDTO dto = DozerMapper.parseObject(entity, OngDTO.class);
        dto.add(linkTo(methodOn(OngController.class).acharOngPorNomeUsuario(nomeUsuario)).withSelfRel());
        return dto;

    }

    public OngDTO create(OngDTO ong) {

        if (ong == null)
            throw new RequiredObjectIsNullException("JSON vazio");

        // se encontrar a ong no bd retorna badrequest
        if (existsOngWithEmail(ong.getEmail().toLowerCase())) {
            throw new IllegalStateException("E-mail já está em uso");
        }
        if (existsOngWithCnpj(ong.getCnpj().getCnpj())) {
            throw new IllegalStateException("CNPJ já está em uso");
        }
        if (existsOngWithCell(ong.getCell())) {
            throw new IllegalStateException("Celular já está em uso");
        }
        if (existsOngWithNomeUsuario(ong.getNomeUsuario())) {
            throw new IllegalStateException("Nome Usuário já está em uso");
        }
        ong.setSenha(passwordEncoder.encode(ong.getSenha()));
        Ong entity = DozerMapper.parseObject(ong, Ong.class);
        entity.setCnpj(ong.getCnpj().getCnpj());
        entity.setEmail(ong.getEmail().toLowerCase());
        entity.setRole(Roles.ONG);
        OngDTO dto = DozerMapper.parseObject(ongRepository.save(entity), OngDTO.class);
        dto.add(linkTo(methodOn(OngController.class).acharOngPorId(dto.getKey())).withSelfRel());
        return dto;
    }

    public TokenDTO logar(LoginDTO data) {

        String identifier = data.identifier();

        //Se for um e-mail (tem '@'), transforma para lowercase
        if (identifier.contains("@")) {
            identifier = identifier.toLowerCase();
        }

        // credenciais do spring security
        var usernamePassword = new UsernamePasswordAuthenticationToken(identifier, data.password());

        // autentica de forma milagrosa as credenciais
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((LoginIdentityView) auth.getPrincipal());

        return new TokenDTO(token);
    }

    public OngDTO update(OngDTO ong, Long id) {

        if (ong == null)
            throw new RequiredObjectIsNullException();

        ong.setSenha(passwordEncoder.encode(ong.getSenha()));

        Ong entity = ongRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Ong não encontrado."));

        entity.setNome(ong.getNome());
        entity.setNomeUsuario(ong.getNomeUsuario());
        entity.setFotoPerfil(ong.getFotoPerfil());
        entity.setEmail(ong.getEmail().toLowerCase());
        entity.setSenha(ong.getSenha());
        entity.setEndereco(ong.getEndereco());
        entity.setCell(ong.getCell());
        entity.setCnpj(ong.getCnpj().getCnpj());
        entity.setResponsavel(ong.getResponsavel());

        OngDTO dto = DozerMapper.parseObject(ongRepository.save(entity), OngDTO.class);
        dto.add(linkTo(methodOn(OngController.class).acharOngPorId(dto.getKey())).withSelfRel());
        return dto;
    }

    public void delete(Long id) {
        ongRepository.deleteById(id);
    }

    public PagedModel<EntityModel<AdocaoDTO>> findAllAdocoesByOngId(Long idOng, Pageable pageable) {

        Page<Adocao> adocaoPage = adocaoRepository.findAdocoesByOngId(idOng, pageable);

        Page<AdocaoDTO> adocaoDtoPage = adocaoPage.map(a -> DozerMapper.parseObject(a, AdocaoDTO.class));

        adocaoDtoPage = adocaoDtoPage.map(
                dto -> dto.add(linkTo(methodOn(AdocaoController.class).acharAdocaoPorId(dto.getKey())).withSelfRel()));

        Link selfLink = linkTo(methodOn(OngController.class)
                .listarAdocoesPorOngId(idOng, pageable.getPageNumber(), pageable.getPageSize(), "asc"))
                .withSelfRel();

        return adocaoDtoAssembler.toModel(adocaoDtoPage, selfLink);
    }

    public OngDTO partialUpdate(Long id, Map<String, Object> updates) {
        Ong ong = ongRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Ong não encontrada."));

        ObjectMapper mapper = new ObjectMapper();
        updates.forEach((campo, valor) -> {
            Field field = ReflectionUtils.findField(Ong.class, campo);
            if (field != null) {
                field.setAccessible(true);

                if(campo.equalsIgnoreCase("email") && valor instanceof String) {
                    valor = ((String) valor).toLowerCase();
                }
                ReflectionUtils.setField(field, ong, mapper.convertValue(valor, field.getType()));
            }
        });

        // Mapeia a entidade para o DTO
        OngDTO ongDTO = DozerMapper.parseObject(ong, OngDTO.class);

        // Faz a validação do DTO
        Set<ConstraintViolation<OngDTO>> violations = validator.validate(ongDTO);

        // Se houver erros de validação, lança uma exceção
        if (!violations.isEmpty()) {
            StringBuilder errors = new StringBuilder();
            for (ConstraintViolation<OngDTO> violation : violations) {
                errors.append(violation.getMessage());
            }
            throw new ConstraintViolationException("Erro de validação: " + errors.toString(), violations);
        }

        ongRepository.save(ong);
        return DozerMapper.parseObject(ong, OngDTO.class);
    }

    public boolean existsOngWithEmail(String email) {
        return ongRepository.findByEmail(email).isPresent();
    }

    public boolean existsOngWithCnpj(String cnpj) {
        return ongRepository.findByCnpj(cnpj).isPresent();
    }

    public boolean existsOngWithNomeUsuario(String nomeUsuario) {
        return ongRepository.findByNomeUsuario(nomeUsuario).isPresent();
    }

    public boolean existsOngWithCell(String cell) {
        return ongRepository.findByCell(cell).isPresent();
    }
}