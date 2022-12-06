package com.example.gbdpbootcore.page;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * @Author: kongweichang
 * @Date: 2019/10/9 14:40
 */
@Component
public class StringToMapConverter implements Converter<String, Map<String, Integer>> {
    @Override
    public Map<String, Integer> convert(String source) {
        try {
            return new ObjectMapper().readValue(source, new TypeReference<Map<String, Integer>>() {});
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
