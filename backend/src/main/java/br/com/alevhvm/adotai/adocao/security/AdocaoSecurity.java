package br.com.alevhvm.adotai.adocao.security;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import br.com.alevhvm.adotai.adocao.service.AdocaoService;
import br.com.alevhvm.adotai.animal.service.AnimalService;
import br.com.alevhvm.adotai.ong.service.OngService;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component("adocaoSecurity")
public class AdocaoSecurity {
    
    private final AdocaoService adocaoService;

    private final AnimalService animalService;

    private final OngService ongService;

    public boolean podeVisualizar(Long adocaoId, Authentication authentication) {
        var adocao = adocaoService.findById(adocaoId);
        String emailDoUsuarioLogado = authentication.getName();

        var animal = animalService.findById(adocao.getIdAnimal());
        var ong = ongService.findById(animal.getIdOng());

        return adocao.getEmailUsuario().equals(emailDoUsuarioLogado) || ong.getEmail().equals(emailDoUsuarioLogado);
    }

    public boolean ongDonaDaAdocao(Long adocaoId, Authentication authentication) {
        var adocao = adocaoService.findById(adocaoId);
        String email = authentication.getName();

        var animal = animalService.findById(adocao.getIdAnimal());
        var ong = ongService.findById(animal.getIdOng());

        return ong.getEmail().equals(email);
    }
}
