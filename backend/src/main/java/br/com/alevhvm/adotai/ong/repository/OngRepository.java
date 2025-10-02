package br.com.alevhvm.adotai.ong.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.alevhvm.adotai.common.enums.StatusConta;
import br.com.alevhvm.adotai.ong.model.Ong;

@Repository
public interface OngRepository extends JpaRepository<Ong, Long>, OngRepositoryCustom {

    Optional<Ong> findByEmail(String email);

    Optional<Ong> findByNomeUsuario(String nomeUsuario);

    Optional<Ong> findByCnpj(String cnpj);

    Optional<Ong> findByCell(String cell);

    void deleteByNomeUsuario(String nomeUsuario);

    Page<Ong> findAllByStatus(StatusConta status, Pageable pageable);

    @Modifying
    @Query("UPDATE Ong o SET o.status = 'INATIVA' WHERE o.nomeUsuario = :nomeUsuario")
    void desativarOng(String nomeUsuario);

    @Modifying
    @Query("UPDATE Ong o SET o.status = 'ATIVA' WHERE o.nomeUsuario = :nomeUsuario")
    void ativarOng(String nomeUsuario);

    @Modifying
    @Query("UPDATE Ong o SET o.status = 'BLOQUEADA' WHERE o.nomeUsuario = :nomeUsuario")
    void bloquearOng(String nomeUsuario);
}