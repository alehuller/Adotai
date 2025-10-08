package br.com.alevhvm.adotai.avaliacao.repository;

import java.util.List;
import java.util.Map;

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
    
    @Query("SELECT a.ong.id AS ongId, AVG(a.nota) AS media FROM Avaliacao a GROUP BY a.ong.id ORDER BY a.ong.id")
    List<Map<String, Object>> calcularMediaPorTodasOngs();
}
