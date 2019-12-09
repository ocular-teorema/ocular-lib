package ru.ddg.stalt.ocular.lib.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:ocular-lib.properties")
public class OcularLibConfiguration {
    @Bean
    public ObjectMapper producerJackson2MessageConverter() {
        return new ObjectMapper();
    }
}
