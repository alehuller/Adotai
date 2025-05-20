package br.com.alevh.sistema_adocao_pets.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.alevh.sistema_adocao_pets.model.Usuario;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

        @Query("SELECT u FROM Usuario u WHERE u.email =:email")
        Usuario findByUsername(@Param("email") String email);
}
