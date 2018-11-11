package com.mghostl.comfortablechanges.rest;

import com.mghostl.comfortablechanges.AbstractTest;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RatesControllerTest extends AbstractTest {

    private static final double DELTA = 1e-10;

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
        Rates rates = new Rates().add(new Item(from, to, in, out, amount));
        rates.setExchange(new Exchange(exchangeName, exchangeURL));
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
}