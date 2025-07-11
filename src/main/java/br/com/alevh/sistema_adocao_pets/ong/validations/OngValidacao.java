package br.com.alevh.sistema_adocao_pets.ong.validations;

import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import br.com.alevh.sistema_adocao_pets.common.vo.EnderecoVO;
import br.com.alevh.sistema_adocao_pets.ong.dto.OngDTO;
import br.com.alevh.sistema_adocao_pets.common.exceptions.RequiredObjectIsNullException;
import br.com.alevh.sistema_adocao_pets.ong.model.Ong;
import br.com.alevh.sistema_adocao_pets.ong.repository.OngRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OngValidacao {

    private final OngRepository ongRepository;

    public void validate(OngDTO ong) {
        if (ong == null)
            throw new RequiredObjectIsNullException("Não há dados");

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
    }

    public void validateUpdate(Ong entity) {
        if (existsOngWithEmail(entity.getEmail().toLowerCase())) {
            throw new IllegalStateException("E-mail já está em uso");
        }
        if (existsOngWithCell(entity.getCell())) {
            throw new IllegalStateException("Celular já está em uso");
        }
        if (existsOngWithNomeUsuario(entity.getNomeUsuario())) {
            throw new IllegalStateException("Nome Usuário já está em uso");
        }
    }

    public void validatePartialUpdate(String nomeUsuario, Map<String, Object> updates) {
        if (updates.containsKey("email")) {
            String email = updates.get("email").toString().toLowerCase();
            Optional<Ong> ongExistente = ongRepository.findByEmail(email);
            if (ongExistente.isPresent() && !ongExistente.get().getNomeUsuario().equals(nomeUsuario)) {
                throw new IllegalStateException("E-mail já está em uso por outra ong");
            }
        }

        if (updates.containsKey("cnpj")) {
            throw new IllegalArgumentException("Não é permitido alterar o CNPJ.");
        }

        if (updates.containsKey("cell")) {
            String cell = updates.get("cell").toString();
            Optional<Ong> ongExistente = ongRepository.findByCell(cell);
            if (ongExistente.isPresent() && !ongExistente.get().getNomeUsuario().equals(nomeUsuario)) {
                throw new IllegalStateException("Celular já está em uso por outra ong");
            }
        }

        if (updates.containsKey("nomeUsuario")) {
            String nomeUsuario2 = updates.get("nomeUsuario").toString();
            Optional<Ong> ongExistente = ongRepository.findByNomeUsuario(nomeUsuario2);
            if (ongExistente.isPresent() && !ongExistente.get().getNomeUsuario().equals(nomeUsuario)) {
                throw new IllegalStateException("Nome de usuário já está em uso por outra ong");
            }
        }
    }

    public void validarEnderecoPreenchido(EnderecoVO endereco) {
        if (isNullOrBlank(endereco.getLogradouro())) {
            throw new IllegalArgumentException("Logradouro não encontrado para o CEP informado");
        }
        if (isNullOrBlank(endereco.getBairro())) {
            throw new IllegalArgumentException("Bairro não encontrado para o CEP informado");
        }
        if (isNullOrBlank(endereco.getCidade())) {
            throw new IllegalArgumentException("Cidade não encontrada para o CEP informado");
        }
        if (isNullOrBlank(endereco.getEstado())) {
            throw new IllegalArgumentException("Estado não encontrado para o CEP informado");
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
