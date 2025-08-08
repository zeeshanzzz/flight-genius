package com.example.flightgenius.duffel.config;

import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DuffelFeignConfig {
    @Value("${duffel.api-key}")
    private String apiKey;

    @Bean
    public RequestInterceptor duffelRequestInterceptor() {
        return requestTemplate -> {
            requestTemplate.header("Authorization", "Bearer " + apiKey);
            requestTemplate.header("Duffel-Version", "v2");
            requestTemplate.header("Content-Type", "application/json");
        };
    }
}
