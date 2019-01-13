package com.mghostl.comfortablechanges.rest;

import com.mghostl.comfortablechanges.AbstractTest;
import com.mghostl.comfortablechanges.dao.Currency;
import com.mghostl.comfortablechanges.dao.Exchange;
import com.mghostl.comfortablechanges.dao.Item;
import com.mghostl.comfortablechanges.dao.Rates;
import com.mghostl.comfortablechanges.db.RatesStorage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RatesControllerTest extends AbstractTest {

    private static final double DELTA = 1e-10;
    private final Currency[] currencies = new Currency[] { new Currency("USD", "USD_IMAGE"),
            new Currency("EUR", "EUR_IMAGE"),
            new Currency("WMZ", null),
            new Currency("RUR", null)};

    @MockBean
    private RatesStorage ratesStorage;

    @Override
    @Before
    public void setUp() {
        super.setUp();
    }

    @Test
    public void shouldReturnExchangesForTwoCurrencies() throws Exception {
        String from = "USD";
        String to = "EUR";
        double in = 1;
        double out = 2;
        double amount = 1000;
        String exchangeName = "TestExchange";
        String exchangeURL = "http://localhost";
        String ref = "GoHere";
        Rates rates = new Rates().add(new Item(from, to, in, out, amount));
        Exchange expectedExchange = new Exchange(exchangeName, exchangeURL);
        expectedExchange.setRef(ref);
        rates.setExchange(expectedExchange);
        ArrayList<Rates> exchanges = new ArrayList<>();
        exchanges.add(rates);
        given(ratesStorage.getExchanges(from, to)).willReturn(exchanges.toArray(new Rates[]{}));

        mvc.perform(get("/rates")
                .param("to", to)
                .param("from", from)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String resultStr = result.getResponse().getContentAsString();
                    JSONArray root = new JSONArray(resultStr);
                    assertEquals(1, root.length());
                    JSONObject resultRates = root.getJSONObject(0);
                    JSONObject exchange = resultRates.getJSONObject("exchange");
                    assertNotNull(exchange);
                    assertEquals(exchangeName, exchange.getString("name"));
                    assertEquals(exchangeURL, exchange.getString("url"));
                    assertEquals(ref, exchange.getString("ref"));
                    JSONArray resultItems = resultRates.getJSONArray("items");
                    assertEquals(1, resultItems.length());
                    JSONObject resultItem = resultItems.getJSONObject(0);
                    assertEquals(1000, resultItem.getInt("amount"));
                    assertEquals(1, resultItem.getDouble("in"), DELTA);
                    assertEquals("USD", resultItem.getString("from"));
                    assertEquals("EUR", resultItem.get("to"));
                    assertEquals(2, resultItem.getDouble("out"), DELTA);
                });
    }

    @Test
    public void shouldReturnFromCurrencies() throws Exception {
        given(ratesStorage.getFrom()).willReturn(currencies);

        mvc.perform(get("/from")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                   String resultStr = result.getResponse().getContentAsString();
                   assertArrayEquals(currencies, mapFromJson(resultStr, Currency[].class));
                });
    }

    @Test
    public void shouldReturnToCurrencies() throws Exception {

        String from = "BTC";

        given(ratesStorage.getTo(from)).willReturn(currencies);

        mvc.perform(get("/to")
            .param("from", from)
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String resultStr = result.getResponse().getContentAsString();
                    assertArrayEquals(currencies, (mapFromJson(resultStr, Currency[].class)));
                });
    }

    @Test
    public void shouldReturnCurrenceis() throws Exception {
        String USD = "USD";
        String EUR = "EUR";
        String USD_IMAGE = "USD Image";
        String EUR_IMAGE = "EUR Image";
        Currency[] currencies = new Currency[]{new Currency(USD, USD_IMAGE), new Currency(EUR, EUR_IMAGE)};

        given(ratesStorage.getCurrencies()).willReturn(currencies);

        mvc.perform(get("/currencies")
        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String resultStr = result.getResponse().getContentAsString();
                    assertArrayEquals(currencies, mapFromJson(resultStr, Currency[].class));
                });
    }

}