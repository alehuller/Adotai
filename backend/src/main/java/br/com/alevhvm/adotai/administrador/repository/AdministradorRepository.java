package br.com.alevhvm.adotai.administrador.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.alevhvm.adotai.administrador.model.Administrador;
import br.com.alevhvm.adotai.common.enums.StatusConta;

@Repository
public interface AdministradorRepository extends JpaRepository<Administrador, Long> {
    Optional<Administrador> findByEmail(String email);

    Optional<Administrador> findByNomeUsuario(String nomeUsuario);

    void deleteByNomeUsuario(String nomeUsuario);

    Optional<Administrador> findByCell(String cell);

    Page<Administrador> findAllByStatus(StatusConta status, Pageable pageable);

    @Modifying
    @Query("UPDATE Administrador a SET a.status = 'INATIVA' WHERE a.nomeUsuario = :nomeUsuario")
    void desativarAdministrador(String nomeUsuario);

    @Modifying
    @Query("UPDATE Administrador a SET a.status = 'ATIVA' WHERE a.nomeUsuario = :nomeUsuario")
    void ativarAdministrador(String nomeUsuario);

    @Modifying
    @Query("UPDATE Administrador a SET a.status = 'BLOQUEADA' WHERE a.nomeUsuario = :nomeUsuario")
    void bloquearAdministrador(String nomeUsuario);

}
