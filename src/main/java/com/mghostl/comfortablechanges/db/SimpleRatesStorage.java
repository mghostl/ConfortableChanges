package com.mghostl.comfortablechanges.db;

import com.mghostl.comfortablechanges.dao.Exchange;
import com.mghostl.comfortablechanges.dao.Item;
import com.mghostl.comfortablechanges.dao.Rates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SimpleRatesStorage implements RatesStorage{
    private final Logger LOGGER = LoggerFactory.getLogger(SimpleRatesStorage.class);
    private final Set<Rates> rates = ConcurrentHashMap.newKeySet();

    public void addRates(Exchange exchange, Rates rates) {
        LOGGER.info("Updated rates for the exchange: {}", exchange);
        rates.setExchange(exchange);
        this.rates.add(rates);
    }

    @Cacheable("exchanges")
    public Rates[] getExchanges(String from, String to) {
        Rates[] result = rates.stream()
                .filter(rates -> rates.getItems()
                                        .stream()
                                        .anyMatch(item -> isNeededItem(item, from, to)))
                .map(rates -> filterRates(from, to, rates))
                .toArray(Rates[]::new);
        LOGGER.debug("getExchanges({},{}) result: {}", from, to, result);
        return result;
    }

    private Rates filterRates(String from, String to, Rates rates) {
        return rates.getItems().stream()
                .filter(item -> isNeededItem(item, from, to))
                .collect(Rates::new, Rates::add, Rates::addAllItems)
                .copyExchange(rates);
    }

    private boolean isNeededItem(Item item, String from, String to) {
        return item.getFrom().equalsIgnoreCase(from) && item.getTo().equalsIgnoreCase(to);
    }
}