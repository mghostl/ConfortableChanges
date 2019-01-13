package com.mghostl.comfortablechanges.db;

import com.mghostl.comfortablechanges.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SimpleRatesStorage implements RatesStorage{
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleRatesStorage.class);
    private final Set<Rates> rates = ConcurrentHashMap.newKeySet();
    private Map<String, String> imagesByCurrencies = new ConcurrentHashMap<>();
    private ImagesHolder imagesHolder;

    @Autowired
    public SimpleRatesStorage(ImagesHolder imagesHolder) {
        this.imagesHolder = imagesHolder;
    }

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
    public Currency[] getCurrencies() {
        Set<Currency> currencies = new HashSet<>();
        rates.forEach(rate -> rate.getItems()
                .forEach(item -> {
                    fillImages(item);
                    addCurrency(currencies, item.getFrom().toUpperCase());
                    addCurrency(currencies, item.getTo().toUpperCase());
                }));
        return currencies.toArray(new Currency[]{});
    }

    @Override
    @Cacheable("from")
    public Currency[] getFrom() {
        Set<Currency> currencies = new HashSet<>();
        rates.forEach(rate -> rate.getItems().forEach(item -> {
            fillImages(item);
            if(item.getFrom() != null && !item.getFrom().isEmpty()) {
                addCurrency(currencies, item.getFrom());
            }
        }));
        return currencies.toArray(new Currency[]{});
    }

    @Cacheable("to")
    @Override
    public Currency[] getTo(String from) {
        Set<Currency> currencies = new HashSet<>();
        rates.forEach(rate -> rate.getItems().stream().filter(item -> item.getFrom().equals(from))
                        .forEach(item -> {
                            fillImages(item);
                            addCurrency(currencies, item.getTo());
                        }));
        return currencies.toArray(new Currency[] {});
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

    private void fillImages(Item item) {
            if(!imagesByCurrencies.containsKey(item.getFrom())) {
                addImage(item.getFrom());
            }
            if(!imagesByCurrencies.containsKey(item.getTo())) {
                addImage(item.getTo());
            }
    }

    private void addImage(String currency) {
        imagesHolder.getImage(currency)
                .ifPresent(source -> imagesByCurrencies.put(currency, source));
    }

    private void addCurrency(Set<Currency> currencies, String currency) {
        currencies.add(new Currency(currency, imagesByCurrencies.get(currency)));
    }

}