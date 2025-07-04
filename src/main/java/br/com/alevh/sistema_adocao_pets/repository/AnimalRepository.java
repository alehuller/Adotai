package br.com.alevh.sistema_adocao_pets.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.alevh.sistema_adocao_pets.data.dto.v1.AnimalFiltroDTO;
import br.com.alevh.sistema_adocao_pets.model.Animal;

@Repository
public interface AnimalRepository extends JpaRepository<Animal, Long> {

    Optional<Animal> findByNome(String nome);

    void deleteByNome(String nome);

    //@Query("SELECT a FROM Animal a WHERE a.ong.nomeUsuario = :nomeUsuario")
    Page<Animal> findByOngNomeUsuario(String nomeUsuario, Pageable pageable);
@Query(value = """
    SELECT * FROM animal a
    JOIN ong o ON a.ong_id = o.id
    WHERE (:#{#filtro.especie} IS NULL OR a.especie = :#{#filtro.especie})
    AND (:#{#filtro.raca} IS NULL OR a.raca = :#{#filtro.raca})
    AND (:#{#filtro.porte} IS NULL OR a.porte = :#{#filtro.porte})
    AND (:#{#filtro.sexo} IS NULL OR a.sexo = :#{#filtro.sexo})
    AND (:#{#filtro.status} IS NULL OR a.status = :#{#filtro.status})
    AND (
        :#{#filtro.cidadeOng} IS NULL 
        OR o.endereco::jsonb ->> 'cidade' ILIKE CONCAT('%', :#{#filtro.cidadeOng}, '%')
    )
""",
countQuery = """
    SELECT COUNT(*) FROM animal a
    JOIN ong o ON a.ong_id = o.id
    WHERE (:#{#filtro.especie} IS NULL OR a.especie = :#{#filtro.especie})
    AND (:#{#filtro.raca} IS NULL OR a.raca = :#{#filtro.raca})
    AND (:#{#filtro.porte} IS NULL OR a.porte = :#{#filtro.porte})
    AND (:#{#filtro.sexo} IS NULL OR a.sexo = :#{#filtro.sexo})
    AND (:#{#filtro.status} IS NULL OR a.status = :#{#filtro.status})
    AND (
        :#{#filtro.cidadeOng} IS NULL 
        OR o.endereco::jsonb ->> 'cidade' ILIKE CONCAT('%', :#{#filtro.cidadeOng}, '%')
    )
""",
nativeQuery = true)
Page<Animal> filtrarAnimal(@Param("filtro") AnimalFiltroDTO filtro, Pageable pageable);


}