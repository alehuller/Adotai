package br.com.alevh.sistema_adocao_pets.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.alevh.sistema_adocao_pets.data.dto.v1.OngFiltroDTO;
import br.com.alevh.sistema_adocao_pets.model.Ong;

public interface OngRepositoryCustom {
    Page<Ong> filtrarOngsNativo(OngFiltroDTO filtro, Pageable pageable);
}
