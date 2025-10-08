package br.com.alevhvm.adotai.avaliacao.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.alevhvm.adotai.avaliacao.model.Avaliacao;

@Repository
public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long>{

    @Query("SELECT AVG(a.nota) FROM Avaliacao a WHERE a.ong.id = :id")
    Double calcularMediaByOngId(@Param("id") Long id);

    @Query("SELECT a FROM Avaliacao a WHERE a.ong.id = :id")
    Page<Avaliacao> findAvaliacoesByOngId(@Param("id") Long id, Pageable pageable);
    
}
