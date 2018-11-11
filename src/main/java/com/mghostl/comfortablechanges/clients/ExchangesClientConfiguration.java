package com.mghostl.comfortablechanges.clients;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration(value = "exchangesClient")
@PropertySource(value = "classpath:exchanges-client.properties")
@ConfigurationProperties(prefix = "exchanges.client")
public class ExchangesClientConfiguration {

    @Getter
    @Setter
    private String userAgent;

}