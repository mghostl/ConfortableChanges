package com.mghostl.comfortablechanges.db;

import com.mghostl.comfortablechanges.dao.Rates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SimpleRatesStorage implements RatesStorage{
    private final Logger LOGGER = LoggerFactory.getLogger(SimpleRatesStorage.class);
    private final Map<String, Rates> ratesByExchange = new ConcurrentHashMap<>();

    public void addRates(String exchange, Rates rates) {
        LOGGER.info("Updated rates for the exchange: {}", exchange);
        ratesByExchange.put(exchange, rates);
    }

    @Cacheable("exchanges")
    public Map<String, Rates> getExchanges(String from, String to) {
        Map<String, Rates> result = new ConcurrentHashMap<>();
        ratesByExchange.keySet().stream()
                .filter(exchange -> ratesByExchange.get(exchange)
                                                    .getItems()
                                                    .stream()
                                                    .anyMatch(item -> item.getFrom().equalsIgnoreCase(from) && item.getTo().equalsIgnoreCase(to)))
                .forEach(exchange -> result.put(exchange, ratesByExchange.get(exchange)));
        return result;
    }
}