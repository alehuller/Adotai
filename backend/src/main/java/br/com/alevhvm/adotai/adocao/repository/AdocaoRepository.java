package br.com.alevhvm.adotai.adocao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.alevhvm.adotai.adocao.model.Adocao;

@Repository
public interface AdocaoRepository extends JpaRepository<Adocao, Long> {

    @Query("SELECT a FROM Adocao a WHERE a.usuario.id = :idUsuario")
    Page<Adocao> findAdocoesByUsuarioId(@Param("idUsuario") Long idUsuario, Pageable pageable);

    @Query("SELECT a FROM Adocao a WHERE a.animal.ong.id = :idOng")
    Page<Adocao> findAdocoesByOngId(@Param("idOng") Long idOng, Pageable pageable);

    @Query("SELECT a FROM Adocao a WHERE a.usuario.nomeUsuario = :nomeUsuario")
    Page<Adocao> findAdocoesByNomeUsuario(@Param("nomeUsuario") String nomeUsuario, Pageable pageable);
}