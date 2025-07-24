package br.com.alevhvm.adotai.administrador.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.alevhvm.adotai.administrador.model.Administrador;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    Optional<Administrador> findByEmail(String email);

    Optional<Administrador> findByNomeUsuario(String nomeUsuario);

    void deleteByNomeUsuario(String nomeUsuario);

    Optional<Administrador> findByCell(String cell);

}
