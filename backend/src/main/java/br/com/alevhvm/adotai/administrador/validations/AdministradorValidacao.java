package br.com.alevhvm.adotai.administrador.validations;

import br.com.alevhvm.adotai.administrador.dto.AdministradorDTO;
import br.com.alevhvm.adotai.administrador.model.Administrador;
import br.com.alevhvm.adotai.administrador.repository.AdministradorRepository;
import br.com.alevhvm.adotai.common.exceptions.ValidacaoException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AdministradorValidacao {

    private final AdministradorRepository administradorRepository;

    public boolean existsAdministradorWithEmail(String email) {
        return administradorRepository.findByEmail(email).isPresent();
    }

    public boolean existsAdministradorWithNomeUsuario(String nomeUsuario) {
        return administradorRepository.findByNomeUsuario(nomeUsuario).isPresent();
    }

    public boolean existsAdministradorWithCell(String cell) {
        return administradorRepository.findByCell(cell).isPresent();
    }

    public void validate(AdministradorDTO admin) {

        if (admin == null) {
            throw new NullPointerException("Não há dados");
        }

        List<String> erros = new ArrayList<>();

        if (existsAdministradorWithEmail(admin.getEmail().toLowerCase())) {
            erros.add("E-mail já está em uso");
        }
        if (existsAdministradorWithNomeUsuario(admin.getNomeUsuario())) {
            erros.add("Nome de Usuário já está em uso");
        }
        if (existsAdministradorWithCell(admin.getCell())) {
            erros.add("Cell já está em uso");
        }

        if (!erros.isEmpty()) {
            throw new ValidacaoException(erros);
        }
    }

    public void validateUpdate(Administrador entity) {
        List<String> erros = new ArrayList<>();

        if (existsAdministradorWithEmail(entity.getEmail().toLowerCase())) {
            erros.add("E-mail já está em uso");
        }
        if (existsAdministradorWithCell(entity.getCell())) {
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
            Optional<Administrador> usuarioExistente = administradorRepository.findByEmail(email);
            if (usuarioExistente.isPresent() && !usuarioExistente.get().getNomeUsuario().equals(nomeUsuario)) {
                erros.add("E-mail já está em uso por outro administrador");
            }
        }

        if (updates.containsKey("nomeUsuario")) {
            erros.add("Não é permitido alterar o nome de usuário.");
        }

        if (updates.containsKey("cell")) {
            String cell = updates.get("cell").toString();
            Optional<Administrador> usuarioExistente = administradorRepository.findByCell(cell);
            if (usuarioExistente.isPresent() && !usuarioExistente.get().getNomeUsuario().equals(nomeUsuario)) {
                erros.add("Celular já está em uso por outro administrador");
            }
        }

        if (!erros.isEmpty()) {
            throw new ValidacaoException(erros);
        }
    }
}
