package com.mghostl.comfortablechanges.clients;

import java.io.BufferedReader;
import java.net.URL;
import java.util.Optional;

public interface ExchangesClient {
    Optional<BufferedReader> getExchanges(URL url);
}