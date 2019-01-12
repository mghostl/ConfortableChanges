package com.mghostl.comfortablechanges.db;

import com.mghostl.comfortablechanges.dao.Exchange;
import com.mghostl.comfortablechanges.dao.Item;
import com.mghostl.comfortablechanges.dao.Rates;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SimpleRatesStorage implements RatesStorage{
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleRatesStorage.class);
    private final Set<Rates> rates = ConcurrentHashMap.newKeySet();

    public void addRates(Exchange exchange, Rates rates) {
        LOGGER.debug("Updated rates for the exchange: {}", exchange);
        rates.setExchange(exchange);
        Optional<Rates> existsRates = getRatesForExchange(exchange.getName());
        if(existsRates.isPresent()) {
            existsRates.get().replaceAllItems(rates);
            return;
        }
        this.rates.add(rates);
    }

    @Cacheable("exchanges")
    public Rates[] getExchanges(String from, String to) {
        Rates[] result = rates.stream()
                .filter(rate -> rate.getItems()
                                        .stream()
                                        .anyMatch(item -> isNeededItem(item, from, to)))
                .map(rate -> filterRates(from, to, rate))
                .toArray(Rates[]::new);
        LOGGER.debug("getExchanges({},{}) result: {}", from, to, result);
        return result;
    }

    @Override
    public Optional<Rates> getRatesForExchange(String exchangeName) {
        return rates.stream()
                .filter(rate -> rate.getExchange().getName().equalsIgnoreCase(exchangeName))
                .findFirst();
    }

    @Cacheable("currencies")
    public String[] getCurrencies() {
        Set<String> currencies = new HashSet<>();
        rates.forEach(rate -> rate.getItems()
                .forEach(item -> {
                    currencies.add(item.getFrom().toUpperCase());
                    currencies.add(item.getTo().toUpperCase());
                }));
        return currencies.toArray(new String[]{});
    }

    @Override
    @Cacheable("from")
    public String[] getFrom() {
        Set<String> currencies = new HashSet<>();
        rates.forEach(rate -> rate.getItems().forEach(item -> {
            if(item.getFrom() != null && !item.getFrom().isEmpty()) {
                currencies.add(item.getFrom());
            }
        }));
        return currencies.toArray(new String[]{});
    }

    @Cacheable("to")
    @Override
    public String[] getTo(String from) {
        Set<String> currencies = new HashSet<>();
        rates.forEach(rate -> rate.getItems().stream().filter(item -> item.getFrom().equals(from))
                        .forEach(item -> currencies.add(item.getTo())));
        return currencies.toArray(new String[] {});
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