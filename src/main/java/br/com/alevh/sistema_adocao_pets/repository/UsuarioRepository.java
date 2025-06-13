package br.com.alevh.sistema_adocao_pets.repository;

import br.com.alevh.sistema_adocao_pets.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByCpf(String cpf);

    Optional<Usuario> findByCell(String cell);

    Optional<Usuario> findByNomeUsuario(String nomeUsuario);
}
