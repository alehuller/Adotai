package br.com.alevhvm.adotai.common.vo;

import java.io.Serializable;

import org.hibernate.validator.constraints.URL;
import org.springframework.hateoas.RepresentationModel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SiteVO extends RepresentationModel<SiteVO> implements Serializable{
    private static final long serialVersionUID = 1L;

    @URL(message = "O site informado não é válido")
    private String site;

    @URL(message = "O Instagram informado não é válido")
    private String instagram;

    @URL(message = "O Facebook informado não é válido")
    private String facebook;

    @URL(message = "O TikTok informado não é válido")
    private String tiktok;

    @URL(message = "O YouTube informado não é válido")
    private String youtube;

    @URL(message = "O WhatsApp informado não é válido")
    private String whatsapp;

    @URL(message = "O X informado não é válido")
    private String x;

    @URL(message = "O LinkedIn informado não é válido")
    private String linkedin;

}
