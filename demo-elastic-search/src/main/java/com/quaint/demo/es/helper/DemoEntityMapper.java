package com.quaint.demo.es.helper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.geo.CustomGeoModule;

import java.io.IOException;

/**
 * @author quaint
 * @date 2019-12-26 17:52
 */
public class DemoEntityMapper implements EntityMapper {

    private ObjectMapper objectMapper;

    public DemoEntityMapper() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
        objectMapper.registerModule(new CustomGeoModule());
        objectMapper.registerModule(new DemoModule());
    }

    @Override
    public String mapToString(Object object) throws IOException {
        return objectMapper.writeValueAsString(object);
    }

    @Override
    public <T> T mapToObject(String source, Class<T> clazz) throws IOException {
        return objectMapper.readValue(source, clazz);
    }

}
