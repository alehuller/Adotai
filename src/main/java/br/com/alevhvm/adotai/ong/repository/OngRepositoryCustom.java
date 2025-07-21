package br.com.alevhvm.adotai.ong.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.alevhvm.adotai.ong.dto.OngFiltroDTO;
import br.com.alevhvm.adotai.ong.model.Ong;

public interface OngRepositoryCustom {
    Page<Ong> filtrarOngsNativo(OngFiltroDTO filtro, Pageable pageable);
}
