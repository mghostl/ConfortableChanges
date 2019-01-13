package com.mghostl.comfortablechanges.dao;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.Optional;
import java.util.Set;

@Configuration(value = "imagesHolder")
@PropertySource(value = "classpath:currencies.properties")
@ConfigurationProperties(prefix = "images")

public class ImagesHolder {
    @Getter
    @Setter
    private Set<String> sources;

    public Optional<String> getImage(String currency) {
        return sources.stream()
                .filter(source -> source.contains(currency))
                .findAny();
    }
}
