package com.remind.core.infra.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;

@Configuration
public class JsonCustomConfigure {

    static class NullToEmptyStringSerializer extends JsonSerializer<Object> {
        @Override
        public void serialize(Object o, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
            jsonGenerator.writeString("");
        }
    }

    static class CustomObjectMapper extends ObjectMapper {
        private static final long serialVersionUID = 1L;
        public CustomObjectMapper() {
            getSerializerProvider().setNullValueSerializer(new NullToEmptyStringSerializer());
        }
    }
        @Bean
        public MappingJackson2HttpMessageConverter converter() {
            MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
            CustomObjectMapper mapper = new CustomObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            converter.setObjectMapper(mapper);
            return converter;
        }
}
