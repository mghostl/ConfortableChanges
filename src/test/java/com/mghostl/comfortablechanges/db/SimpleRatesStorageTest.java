package com.mghostl.comfortablechanges.db;

import com.mghostl.comfortablechanges.dao.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SimpleRatesStorageTest {

    private SimpleRatesStorage simpleRatesStorage;
    private String from = "USD";
    private String to = "EUR";
    private double in = 1.0;
    private double out = 2.0;
    private double amount = 100;
    private String exchangeName = "TestExchange";
    private String exchangeURL = "http://localhost";
    private String USD_IMAGE = "USD_IMAGE_SOURCE";
    private String EUR_IMAGE = "EUR_IMAGE_SOURCE";

    @Mock
    private ImagesHolder imagesHolder;

    @Before
    public void init() {
        simpleRatesStorage = new SimpleRatesStorage(imagesHolder);
        simpleRatesStorage.addRates(new Exchange(exchangeName, exchangeURL), new Rates().add(new Item(from, to, in, out, amount)));
        simpleRatesStorage.addRates(new Exchange(exchangeName + 2, exchangeURL),
                new Rates().add(new Item(from + 2, to + 2, in * 2, out * 2, amount * 2)));
        when(imagesHolder.getImage(from)).thenReturn(Optional.of(USD_IMAGE));
        when(imagesHolder.getImage(to)).thenReturn(Optional.of(EUR_IMAGE));
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

    @Test
    public void shouldReturnCurrenciesFrom() {
        Currency[] expectedCurrencies = new Currency[] { new Currency("USD2", null),
                new Currency("USD", USD_IMAGE)};

        Currency[] currenciesFrom = simpleRatesStorage.getFrom();

        assertArrayEquals(expectedCurrencies, currenciesFrom);
    }

    @Test
    public void shouldReturnCurrenciesTo() {
        Currency[] expectedCurrencies = new Currency[] { new Currency("EUR", EUR_IMAGE)};

        Currency[] currenciesTo = simpleRatesStorage.getTo(from);

        assertArrayEquals(expectedCurrencies, currenciesTo);
    }

    @Test
    public void shouldReturnCurrencies() {
       Currency[] expectedCurrencies = new Currency[]{
               new Currency("EUR", EUR_IMAGE),
               new Currency("EUR2", null),
               new Currency("USD2", null),
               new Currency("USD", USD_IMAGE)};
       Currency[] currencies = simpleRatesStorage.getCurrencies();
       assertArrayEquals(expectedCurrencies, currencies);
    }
}
