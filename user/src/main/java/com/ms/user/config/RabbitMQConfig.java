package com.ms.user.config;

import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.json.JsonMapper;

public class RabbitMQConfig {

    @Bean
    public JacksonJsonMessageConverter messageConverter() {
        JsonMapper jsonMapper = JsonMapper.builder().build();
        return new JacksonJsonMessageConverter(jsonMapper);
    }
}
