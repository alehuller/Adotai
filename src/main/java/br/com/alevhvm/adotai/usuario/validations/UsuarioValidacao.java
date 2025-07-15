package br.com.alevhvm.adotai.usuario.validations;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.alevhvm.adotai.auth.dto.RegistroDTO;
import br.com.alevhvm.adotai.common.exceptions.RequiredObjectIsNullException;
import br.com.alevhvm.adotai.usuario.model.Usuario;
import br.com.alevhvm.adotai.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsuarioValidacao {

    private final UsuarioRepository usuarioRepository;

    public void validate(RegistroDTO registroDTO) {
        if (registroDTO == null) {
            throw new RequiredObjectIsNullException("Não há dados");
        }
        // se encontrar o usuario no bd retorna badrequest
        if (existsUsuarioWithEmail(registroDTO.getEmail().toLowerCase())) {
            throw new IllegalStateException("E-mail já está em uso");
        }
        if (existsUsuarioWithCpf(registroDTO.getCpf().getCpf())) {
            throw new IllegalStateException("CPF já está em uso");
        }
        if (existsUsuarioWithCell(registroDTO.getCell())) {
            throw new IllegalStateException("Cell já está em uso");
        }
        if (existsUsuarioWithNomeUsuario(registroDTO.getNomeUsuario())) {
            throw new IllegalStateException("Nome de Usuário já está em uso");
        }
    }

    public void validateUpdate(Usuario entity) {
        if (existsUsuarioWithEmail(entity.getEmail().toLowerCase())) {
            throw new IllegalStateException("E-mail já está em uso");
        }
        if (existsUsuarioWithCell(entity.getCell())) {
            throw new IllegalStateException("Cell já está em uso");
        }
        if (existsUsuarioWithNomeUsuario(entity.getNomeUsuario())) {
            throw new IllegalStateException("Nome de Usuário já está em uso");
        }
    }

    public void validatePartialUpdate(String nomeUsuario, Map<String, Object> updates) {
        if (updates.containsKey("email")) {
            String email = updates.get("email").toString().toLowerCase();
            Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(email);
            if (usuarioExistente.isPresent() && !usuarioExistente.get().getNomeUsuario().equals(nomeUsuario)) {
                throw new IllegalStateException("E-mail já está em uso por outro usuário");
            }
        }

        if (updates.containsKey("cpf")) {
            throw new IllegalArgumentException("Não é permitido alterar o CPF.");
        }

        if (updates.containsKey("cell")) {
            String cell = updates.get("cell").toString();
            Optional<Usuario> usuarioExistente = usuarioRepository.findByCell(cell);
            if (usuarioExistente.isPresent() && !usuarioExistente.get().getNomeUsuario().equals(nomeUsuario)) {
                throw new IllegalStateException("Celular já está em uso por outro usuário");
            }
        }

        if (updates.containsKey("nomeUsuario")) {
            String nomeUsuario2 = updates.get("nomeUsuario").toString();
            Optional<Usuario> usuarioExistente = usuarioRepository.findByNomeUsuario(nomeUsuario2);
            if (usuarioExistente.isPresent() && !usuarioExistente.get().getNomeUsuario().equals(nomeUsuario)) {
                throw new IllegalStateException("Nome de usuário já está em uso por outro usuário");
            }
        }
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

    public boolean existsUsuarioWithNomeUsuario(String nomeUsuario) {
        return usuarioRepository.findByNomeUsuario(nomeUsuario).isPresent();
    }
}
