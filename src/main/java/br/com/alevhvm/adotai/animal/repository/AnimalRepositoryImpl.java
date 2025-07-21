package br.com.alevhvm.adotai.animal.repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.com.alevhvm.adotai.animal.dto.AnimalFiltroDTO;
import br.com.alevhvm.adotai.animal.model.Animal;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Repository
public class AnimalRepositoryImpl implements AnimalRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Animal> filtrarAnimaisNativo(AnimalFiltroDTO filtro, Pageable pageable) {
        StringBuilder sql = new StringBuilder("""
                    SELECT a.* FROM animal a
                    JOIN ong o ON a.ong_id = o.id
                    WHERE 1=1
                """);

        StringBuilder countSql = new StringBuilder("""
                    SELECT COUNT(*) FROM animal a
                    JOIN ong o ON a.ong_id = o.id
                    WHERE 1=1
                """);

        Map<String, Object> params = new HashMap<>();

        if (filtro.getNome() != null) {
            sql.append(" AND a.nome = :nome");
            countSql.append(" AND a.nome = :nome");
            params.put("nome", filtro.getNome());
        }

        if (filtro.getEspecie() != null) {
            sql.append(" AND a.especie = :especie");
            countSql.append(" AND a.especie = :especie");
            params.put("especie", filtro.getEspecie());
        }

        if (filtro.getRaca() != null) {
            sql.append(" AND a.raca = :raca");
            countSql.append(" AND a.raca = :raca");
            params.put("raca", filtro.getRaca());
        }

        if (filtro.getPorte() != null) {
            sql.append(" AND a.porte = :porte");
            countSql.append(" AND a.porte = :porte");
            params.put("porte", filtro.getPorte());
        }

        if (filtro.getSexo() != null) {
            sql.append(" AND a.sexo = :sexo");
            countSql.append(" AND a.sexo = :sexo");
            params.put("sexo", filtro.getSexo());
        }

        if (filtro.getStatus() != null) {
            sql.append(" AND a.status = :status");
            countSql.append(" AND a.status = :status");
            params.put("status", filtro.getStatus());
        }

        if (filtro.getCidadeOng() != null) {
            sql.append(" AND o.endereco::jsonb ->> 'cidade' ILIKE :cidade");
            countSql.append(" AND o.endereco::jsonb ->> 'cidade' ILIKE :cidade");
            params.put("cidade", "%" + filtro.getCidadeOng() + "%");
        }

        sql.append(" ORDER BY a.nome ASC");

        Query query = entityManager.createNativeQuery(sql.toString(), Animal.class);
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        params.forEach((k, v) -> {
            query.setParameter(k, v);
            countQuery.setParameter(k, v);
        });

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        @SuppressWarnings("unchecked")
        List<Animal> resultList = query.getResultList();
        Long total = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<>(resultList, pageable, total);
    }
}
