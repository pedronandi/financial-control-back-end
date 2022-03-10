package com.ms.financialcontrol.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    public static final String DATE_FORMAT_UTC = "yyyy-MM-dd";
    public static final LocalDateSerializer LOCAL_DATE_UTC_SERIALIZER = new LocalDateSerializer(DateTimeFormatter.ofPattern(DATE_FORMAT_UTC));

    @Bean
    @Primary
    public ObjectMapper createObjectMapper() {
        JavaTimeModule module = new JavaTimeModule();
        module.addSerializer(LOCAL_DATE_UTC_SERIALIZER);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(module);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return mapper;
    }
}
