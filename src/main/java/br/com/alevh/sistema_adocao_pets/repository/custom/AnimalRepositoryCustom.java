package br.com.alevh.sistema_adocao_pets.repository.custom;

import br.com.alevh.sistema_adocao_pets.data.dto.v1.AnimalFiltroDTO;
import br.com.alevh.sistema_adocao_pets.model.Animal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AnimalRepositoryCustom {
    Page<Animal> filtrarAnimaisNativo(AnimalFiltroDTO filtro, Pageable pageable);
}
