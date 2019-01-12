package com.mghostl.comfortablechanges.services;

import com.mghostl.comfortablechanges.dao.Exchange;
import com.mghostl.comfortablechanges.dao.Rates;
import com.mghostl.comfortablechanges.dao.RatesMarshaller;
import com.mghostl.comfortablechanges.db.RatesStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ExchangesServiceImpl implements ExchangesService{

    private static final Logger logger = LoggerFactory.getLogger(ExchangesServiceImpl.class);

    private final ExchangesServiceConfig exchangesServiceConfig;

    private final RatesMarshaller ratesMarshaller;

    private final RatesStorage ratesStorage;

    private Map<String, Exchange> exchanges = new ConcurrentHashMap<>();

    @PostConstruct
    private void init() {
        exchangesServiceConfig.getSources().forEach((name, url) -> exchanges.put(name, new Exchange(name, url)));
        exchangesServiceConfig.getUrl().forEach((name, url) -> exchanges.get(name).setRef(url));
    }

    @Autowired
    public ExchangesServiceImpl(ExchangesServiceConfig exchangesServiceConfig, RatesMarshaller ratesMarshaller, RatesStorage ratesStorage) {
        this.exchangesServiceConfig = exchangesServiceConfig;
        this.ratesMarshaller = ratesMarshaller;
        this.ratesStorage = ratesStorage;
    }

    @PostConstruct
    @Scheduled(fixedDelay = 1000)
    public void update() {
        logger.info("update Rates for exchanges: {}", exchanges);
        exchanges.values().forEach(exchange -> getRates(exchange.getUrl())
                .join()
                .ifPresent(rates -> {
                    logger.debug("Received rates: {}", rates);
                    if(rates.getItems() == null) {
                        rates.setItems(new ArrayList<>());
                        logger.error("There is no any items for the exchange: {}", exchange.getName());
                    }
                    ratesStorage.addRates(exchange, rates);
                }));
    }

    private CompletableFuture<Optional<Rates>> getRates(String url) {
       try {
           return ratesMarshaller.fromXMLHTTPSURL(url);
       }
       catch (Exception e) {
           logger.error("Couldn't get rates from exchange: {} due to: {}", url, e);
           return CompletableFuture.completedFuture(Optional.empty());
       }
    }
}
