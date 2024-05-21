package com.remind.core.infra.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
            //localTIme OBJect를 매핑하는 코드 추가
            JavaTimeModule javaTimeModule = new JavaTimeModule();
            javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));
            javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE));
            registerModule(javaTimeModule);

            getSerializerProvider().setNullValueSerializer(new NullToEmptyStringSerializer());

            configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
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
