package br.com.alevh.sistema_adocao_pets.repository;

import br.com.alevh.sistema_adocao_pets.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByCpf(@Param("cpf") String cpf);

    Optional<Usuario> findByCell(@Param("cell") String cell);

        @Query("SELECT u FROM Usuario u WHERE u.nomeUsuario =:nomeUsuario")
        Optional<Usuario> findUsuarioByNomeUsuario(@Param("nomeUsuario") String nomeUsuario);
}
