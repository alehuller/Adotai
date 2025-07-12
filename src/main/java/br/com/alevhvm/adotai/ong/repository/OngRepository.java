package br.com.alevh.sistema_adocao_pets.ong.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.alevh.sistema_adocao_pets.ong.model.Ong;

@Repository
public interface OngRepository extends JpaRepository<Ong, Long>, OngRepositoryCustom {

    Optional<Ong> findByEmail(String email);

    Optional<Ong> findByNomeUsuario(String nomeUsuario);

    Optional<Ong> findByCnpj(String cnpj);

    Optional<Ong> findByCell(String cell);

    void deleteByNomeUsuario(String nomeUsuario);
}