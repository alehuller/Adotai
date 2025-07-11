package br.com.alevh.sistema_adocao_pets.ong.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.alevh.sistema_adocao_pets.ong.dto.OngFiltroDTO;
import br.com.alevh.sistema_adocao_pets.ong.model.Ong;

public interface OngRepositoryCustom {
    Page<Ong> filtrarOngsNativo(OngFiltroDTO filtro, Pageable pageable);
}
