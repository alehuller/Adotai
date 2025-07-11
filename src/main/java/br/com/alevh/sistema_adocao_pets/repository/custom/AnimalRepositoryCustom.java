package br.com.alevh.sistema_adocao_pets.repository.custom;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.alevh.sistema_adocao_pets.data.dto.v1.AnimalFiltroDTO;
import br.com.alevh.sistema_adocao_pets.model.Animal;

public interface AnimalRepositoryCustom {
    Page<Animal> filtrarAnimaisNativo(AnimalFiltroDTO filtro, Pageable pageable);
}
