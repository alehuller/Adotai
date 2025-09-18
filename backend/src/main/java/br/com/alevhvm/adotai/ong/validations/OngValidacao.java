package br.com.alevhvm.adotai.ong.validations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.alevhvm.adotai.common.exceptions.ValidacaoException;
import br.com.alevhvm.adotai.common.vo.EnderecoVO;
import br.com.alevhvm.adotai.ong.dto.OngDTO;
import br.com.alevhvm.adotai.ong.model.Ong;
import br.com.alevhvm.adotai.ong.repository.OngRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OngValidacao {

    private final OngRepository ongRepository;

    public void validate(OngDTO ong) {
        if (ong == null)
            throw new NullPointerException("Não há dados");

        List<String> erros = new ArrayList<>();

        // se encontrar a ong no bd retorna badrequest
        if (existsOngWithEmail(ong.getEmail().toLowerCase())) {
            erros.add("E-mail já está em uso");
        }
        if (existsOngWithCnpj(ong.getCnpj().getCnpj())) {
            erros.add("CNPJ já está em uso");
        }
        if (existsOngWithCell(ong.getCell())) {
            erros.add("Celular já está em uso");
        }
        if (existsOngWithNomeUsuario(ong.getNomeUsuario())) {
            erros.add("Nome Usuário já está em uso");
        }

        if (!erros.isEmpty()) {
            throw new ValidacaoException(erros);
        }
    }

    public void validateUpdate(Ong entity) {
        List<String> erros = new ArrayList<>();

        if (existsOngWithEmail(entity.getEmail().toLowerCase())) {
            erros.add("E-mail já está em uso");
        }
        if (existsOngWithCell(entity.getCell())) {
            erros.add("Celular já está em uso");
        }

        if (!erros.isEmpty()) {
            throw new ValidacaoException(erros);
        }
    }

    public void validatePartialUpdate(String nomeUsuario, Map<String, Object> updates) {
        List<String> erros = new ArrayList<>();

        if (updates.containsKey("email")) {
            String email = updates.get("email").toString().toLowerCase();
            Optional<Ong> ongExistente = ongRepository.findByEmail(email);
            if (ongExistente.isPresent() && !ongExistente.get().getNomeUsuario().equals(nomeUsuario)) {
                erros.add("E-mail já está em uso por outra ong");
            }
        }

        if (updates.containsKey("cnpj")) {
            erros.add("Não é permitido alterar o CNPJ.");
        }

        if (updates.containsKey("cell")) {
            String cell = updates.get("cell").toString();
            Optional<Ong> ongExistente = ongRepository.findByCell(cell);
            if (ongExistente.isPresent() && !ongExistente.get().getNomeUsuario().equals(nomeUsuario)) {
                erros.add("Celular já está em uso por outra ong");
            }
        }

        if (updates.containsKey("nomeUsuario")) {
            erros.add("Não é permitido alterar o nome de usuário.");
        }

        if (!erros.isEmpty()) {
            throw new ValidacaoException(erros);
        }
    }

    public void validarEnderecoPreenchido(EnderecoVO endereco) {
        List<String> erros = new ArrayList<>();

        if (isNullOrBlank(endereco.getLogradouro())) {
            erros.add("Logradouro não encontrado para o CEP informado");
        }
        if (isNullOrBlank(endereco.getBairro())) {
            erros.add("Bairro não encontrado para o CEP informado");
        }
        if (isNullOrBlank(endereco.getCidade())) {
            erros.add("Cidade não encontrada para o CEP informado");
        }
        if (isNullOrBlank(endereco.getEstado())) {
            erros.add("Estado não encontrado para o CEP informado");
        }

        if (!erros.isEmpty()) {
            throw new ValidacaoException(erros);
        }
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

    private boolean isNullOrBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}
