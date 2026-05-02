package com.project.revhive.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AIContentConfig {

    @Bean
    public RestTemplate restTemplate()
    {
        return  new RestTemplate();
    }
}
