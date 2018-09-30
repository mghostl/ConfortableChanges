package com.mghostl.comfortablechanges.db;

import com.mghostl.comfortablechanges.dao.Item;
import com.mghostl.comfortablechanges.dao.Rates;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.junit.Assert.assertEquals;

public class SimpleRatesStorageTest {

    private SimpleRatesStorage simpleRatesStorage;
    private String from = "USD";
    private String to = "EUR";
    private double in = 1.0;
    private double out = 2.0;
    private double amount = 100;
    private String exchange = "TestExchange";

    @Before
    public void init() {
        simpleRatesStorage = new SimpleRatesStorage();
        simpleRatesStorage.addRates(exchange, new Rates().add(new Item(from, to, in, out, amount)));
        simpleRatesStorage.addRates(exchange + 2, new Rates().add(new Item(from + 2, to + 2, in * 2, out * 2, amount * 2)));
    }

    @Test
    public void shouldReturnExchangesByFromAndTo() {
        Map<String, Rates> expectedResult = new ConcurrentHashMap<>();
        expectedResult.put(exchange, new Rates().add(new Item(from, to, in, out, amount)));

        Map<String, Rates> result = simpleRatesStorage.getExchanges(from, to);

        assertEquals(expectedResult, result);
    }
}
