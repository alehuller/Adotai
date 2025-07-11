package br.com.alevh.sistema_adocao_pets.ong.converter;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alevh.sistema_adocao_pets.common.vo.SiteVO;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class SiteConverter implements AttributeConverter<SiteVO, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(SiteVO siteVO) {
        try {
            return objectMapper.writeValueAsString(siteVO);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Erro ao converter SiteVO para JSON", e);
        }
    }

    @Override
    public SiteVO convertToEntityAttribute(String json) {
        try {
            return objectMapper.readValue(json, SiteVO.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Erro ao converter JSON para SiteVO", e);
        }
    }

}
