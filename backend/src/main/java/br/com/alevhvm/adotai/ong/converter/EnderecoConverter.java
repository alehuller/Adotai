package br.com.alevhvm.adotai.ong.converter;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alevhvm.adotai.common.vo.EnderecoVO;
import br.com.alevhvm.adotai.ong.exception.ConversaoEnderecoParaJsonException;
import br.com.alevhvm.adotai.ong.exception.ConversaoJsonParaEnderecoException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EnderecoConverter implements AttributeConverter<EnderecoVO, String> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(EnderecoVO enderecoVO) {
        try {
            return objectMapper.writeValueAsString(enderecoVO);
        } catch (JsonProcessingException e) {
            throw new ConversaoEnderecoParaJsonException("Erro ao converterEnderecoVO para JSON", e);
        }
    }

    @Override
    public EnderecoVO convertToEntityAttribute(String json) {
        try {
            return objectMapper.readValue(json, EnderecoVO.class);
        } catch (IOException e) {
            throw new ConversaoJsonParaEnderecoException("Erro ao converter JSON para EnderecoVO", e);
        }
    }
}