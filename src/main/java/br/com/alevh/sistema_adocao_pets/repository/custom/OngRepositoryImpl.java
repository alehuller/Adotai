package br.com.alevh.sistema_adocao_pets.repository.custom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import br.com.alevh.sistema_adocao_pets.data.dto.v1.OngFiltroDTO;
import br.com.alevh.sistema_adocao_pets.model.Ong;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import jakarta.persistence.Query;

@Repository
public class OngRepositoryImpl implements OngRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Ong> filtrarOngsNativo(OngFiltroDTO filtro, Pageable pageable) {
        StringBuilder sql = new StringBuilder("""
            SELECT o.* FROM ong o
            WHERE 1=1
        """);

        StringBuilder countSql = new StringBuilder("""
            SELECT COUNT(*) FROM ong o
            WHERE 1=1
        """);

        Map<String, Object> params = new HashMap<>();

        if (filtro.getNome() != null) {
            sql.append(" AND o.nome ILIKE :nome");
            countSql.append(" AND o.nome ILIKE :nome");
            params.put("nome", "%" + filtro.getNome() + "%");
        }

        if (filtro.getCidade() != null) {
            sql.append(" AND o.endereco::jsonb ->> 'cidade' ILIKE :cidade");
            countSql.append(" AND o.endereco::jsonb ->> 'cidade' ILIKE :cidade");
            params.put("cidade", "%" + filtro.getCidade() + "%");
        }

        sql.append(" ORDER BY o.nome ASC");

        Query query = entityManager.createNativeQuery(sql.toString(), Ong.class);
        Query countQuery = entityManager.createNativeQuery(countSql.toString());

        params.forEach((key, value) -> {
            query.setParameter(key, value);
            countQuery.setParameter(key, value);
        });

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        @SuppressWarnings("unchecked")
        List<Ong> ongs = query.getResultList();
        Long total = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<>(ongs, pageable, total);
    }
}
