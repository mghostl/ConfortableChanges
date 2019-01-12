package com.mghostl.comfortablechanges.services;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

@EnableAsync
@Configuration(value = "exchangesService")
@EnableScheduling
@PropertySource(value = "classpath:exchanges-service.properties")
@ConfigurationProperties(prefix = "service")
public class ExchangesServiceConfig {
    private final Map<String, String> sources = new HashMap<>();
    private final Map<String, String> url = new HashMap<>();
    private int delayTimeMs;

    public Map<String, String> getSources() {
        return sources;
    }

    public Map<String, String> getUrl() {return url;}

    public int getDelayTimeMs() {
        return delayTimeMs;
    }

    public void setDelayTimeMs(int delayTimeMs) {
        this.delayTimeMs = delayTimeMs;
    }

    @Bean
    public Executor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2);
        executor.setMaxPoolSize(2);
        executor.setQueueCapacity(500);
        executor.setThreadNamePrefix("ExchangesUpdate-");
        executor.initialize();
        return executor;
    }
}
