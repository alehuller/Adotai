package br.com.alevh.sistema_adocao_pets.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.alevh.sistema_adocao_pets.model.Ong;

@Repository
public interface OngRepository extends JpaRepository<Ong, Long> {

    // @Query("SELECT o FROM Ong o WHERE o.email =:email")
    Optional<Ong> findByEmail(@Param("email") String email);

//    @Query("SELECT o FROM Ong o WHERE o.nomeUsuario =:nomeUsuario")
    Optional<Ong> findByNomeUsuario(@Param("nomeUsuario") String nomeUsuario);


}