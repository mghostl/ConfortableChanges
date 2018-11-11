package com.mghostl.comfortablechanges.clients;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Optional;

@Slf4j
@Service
public class ExchangesClientImpl implements ExchangesClient {

    private final ExchangesClientConfiguration exchangesClientConfiguration;

    @Autowired
    public ExchangesClientImpl(ExchangesClientConfiguration exchangesClientConfiguration) {
        this.exchangesClientConfiguration = exchangesClientConfiguration;
    }

    @Override
    public Optional<BufferedReader> getExchanges(URL url) {
        try {
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            if (connection == null) {
                return Optional.empty();
            }
            connection.setRequestProperty("User-Agent", exchangesClientConfiguration.getUserAgent());
            return Optional.of(new BufferedReader(new InputStreamReader(connection.getInputStream())));
        } catch (IOException e) {
            log.error("Couldn't open httpsConnection with {}: {}", url, e);
        }
        return Optional.empty();
    }
}