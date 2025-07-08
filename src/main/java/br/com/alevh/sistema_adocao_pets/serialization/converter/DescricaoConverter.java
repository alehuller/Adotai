package br.com.alevh.sistema_adocao_pets.serialization.converter;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alevh.sistema_adocao_pets.data.dto.common.DescricaoVO;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class DescricaoConverter implements AttributeConverter<DescricaoVO, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(DescricaoVO descricaoVO) {
        try {
            return objectMapper.writeValueAsString(descricaoVO);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Erro ao converter DescricaoVO para JSON", e);
        }
    }

    @Override
    public DescricaoVO convertToEntityAttribute(String json) {
        try {
            return objectMapper.readValue(json, DescricaoVO.class);
        } catch (IOException e) {
            throw new IllegalArgumentException("Erro ao converter JSON para DescricaoVO", e);
        }
    }

}
