package com.mghostl.comfortablechanges.services;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.HashMap;
import java.util.Map;

@Configuration(value = "exchangesService")
@EnableScheduling
@PropertySource(value = "classpath:exchanges-service.properties")
@ConfigurationProperties(prefix = "service")
public class ExchangesServiceConfig {
    private final Map<String, String> exchanges = new HashMap<>();
    private int delayTimeMs;

    public Map<String, String> getExchanges() {
        return exchanges;
    }

    public int getDelayTimeMs() {
        return delayTimeMs;
    }

    public void setDelayTimeMs(int delayTimeMs) {
        this.delayTimeMs = delayTimeMs;
    }
}
