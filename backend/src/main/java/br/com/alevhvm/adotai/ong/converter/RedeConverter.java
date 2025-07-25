package br.com.alevhvm.adotai.ong.converter;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alevhvm.adotai.common.vo.RedeVO;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RedeConverter implements AttributeConverter<RedeVO, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(RedeVO siteVO) {
        try {
            return objectMapper.writeValueAsString(siteVO);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Erro ao converter RedeVO para JSON", e);
        }
    }

    @Override
    public RedeVO convertToEntityAttribute(String json) {
        try {
            return objectMapper.readValue(json, RedeVO.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Erro ao converter JSON para SiteVO", e);
        }
    }

}
