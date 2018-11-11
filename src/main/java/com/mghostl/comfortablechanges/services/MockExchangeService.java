package com.mghostl.comfortablechanges.services;

import com.mghostl.comfortablechanges.dao.Exchange;
import com.mghostl.comfortablechanges.dao.RatesMarshaller;
import com.mghostl.comfortablechanges.db.RatesStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@ConditionalOnProperty(name = "local")
public class MockExchangeService implements ExchangesService {

    private final RatesStorage ratesStorage;

    private final RatesMarshaller ratesMarshaller;

    private static final String EXCHANGE_NAME = "MockExchange";

    private static final String EXCHANGE_URL = "localhost";

    @Autowired
    public MockExchangeService(RatesStorage ratesStorage, RatesMarshaller ratesMarshaller) {
        this.ratesStorage = ratesStorage;
        this.ratesMarshaller = ratesMarshaller;
    }

    @PostConstruct
    @Override
    public void update() {
        subscribe();
    }

    private void subscribe() {
        ratesMarshaller.fromXML(("classpath:rates.xml"))
                .ifPresent(result -> ratesStorage.addRates(new Exchange(EXCHANGE_NAME, EXCHANGE_URL), result));
    }
}