package com.mghostl.comfortablechanges.db;

import com.mghostl.comfortablechanges.dao.Exchange;
import com.mghostl.comfortablechanges.dao.Item;
import com.mghostl.comfortablechanges.dao.Rates;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SimpleRatesStorageTest {

    private SimpleRatesStorage simpleRatesStorage;
    private String from = "USD";
    private String to = "EUR";
    private double in = 1.0;
    private double out = 2.0;
    private double amount = 100;
    private String exchangeName = "TestExchange";
    private String exchangeURL = "http://localhost";

    @Before
    public void init() {
        simpleRatesStorage = new SimpleRatesStorage();
        simpleRatesStorage.addRates(new Exchange(exchangeName, exchangeURL), new Rates().add(new Item(from, to, in, out, amount)));
        simpleRatesStorage.addRates(new Exchange(exchangeName + 2, exchangeURL),
                new Rates().add(new Item(from + 2, to + 2, in * 2, out * 2, amount * 2)));
    }

    @Test
    public void shouldReturnExchangesByFromAndTo() {
        Rates[] expectedResult = new Rates[]{new Rates().add(new Item(from, to, in, out, amount))};
        expectedResult[0].setExchange(new Exchange(exchangeName, exchangeURL));
        Rates[] result = simpleRatesStorage.getExchanges(from, to);
        for(int i = 0; i < result.length; i++) {
            assertEquals(expectedResult[i], result[i]);
        }
    }
}
