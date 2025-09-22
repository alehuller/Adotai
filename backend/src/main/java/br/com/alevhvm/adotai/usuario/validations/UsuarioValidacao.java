package br.com.alevhvm.adotai.usuario.validations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.alevhvm.adotai.auth.dto.RegistroDTO;
import br.com.alevhvm.adotai.common.exceptions.ValidacaoException;
import br.com.alevhvm.adotai.usuario.exception.UsuarioNuloException;
import br.com.alevhvm.adotai.usuario.model.Usuario;
import br.com.alevhvm.adotai.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsuarioValidacao {

    private final UsuarioRepository usuarioRepository;

    public void validate(RegistroDTO registroDTO) {
        if (registroDTO == null) {
            throw new UsuarioNuloException("Não há dados");
        }

        List<String> erros = new ArrayList<>();

        // se encontrar o usuario no bd retorna badrequest
        if (existsUsuarioWithEmail(registroDTO.getEmail().toLowerCase())) {
            erros.add("E-mail já está em uso");
        }
        if (existsUsuarioWithCpf(registroDTO.getCpf().getCpf())) {
            erros.add("CPF já está em uso");
        }
        if (existsUsuarioWithCell(registroDTO.getCell())) {
            erros.add("Cell já está em uso");
        }
        if (existsUsuarioWithNomeUsuario(registroDTO.getNomeUsuario())) {
            erros.add("Nome de Usuário já está em uso");
        }

        if (!erros.isEmpty()) {
            throw new ValidacaoException(erros);
        }
    }

    public void validateUpdate(Usuario entity) {
        List<String> erros = new ArrayList<>();

        if (existsUsuarioWithEmail(entity.getEmail().toLowerCase())) {
            erros.add("E-mail já está em uso");
        }
        if (existsUsuarioWithCell(entity.getCell())) {
            erros.add("Cell já está em uso");
        }

        if (!erros.isEmpty()) {
            throw new ValidacaoException(erros);
        }
    }

    public void validatePartialUpdate(String nomeUsuario, Map<String, Object> updates) {
        List<String> erros = new ArrayList<>();

        if (updates.containsKey("email")) {
            String email = updates.get("email").toString().toLowerCase();
            Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(email);
            if (usuarioExistente.isPresent() && !usuarioExistente.get().getNomeUsuario().equals(nomeUsuario)) {
                erros.add("E-mail já está em uso por outro usuário");
            }
        }

        if (updates.containsKey("cpf")) {
            erros.add("Não é permitido alterar o CPF.");
        }

        if (updates.containsKey("cell")) {
            String cell = updates.get("cell").toString();
            Optional<Usuario> usuarioExistente = usuarioRepository.findByCell(cell);
            if (usuarioExistente.isPresent() && !usuarioExistente.get().getNomeUsuario().equals(nomeUsuario)) {
                erros.add("Celular já está em uso por outro usuário");
            }
        }

        if (updates.containsKey("nomeUsuario")) {
            erros.add("Não é permitido alterar o nome de usuário.");
        }

        if (!erros.isEmpty()) {
            throw new ValidacaoException(erros);
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
