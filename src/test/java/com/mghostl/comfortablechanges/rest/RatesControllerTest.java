package com.mghostl.comfortablechanges.rest;

import com.jayway.jsonpath.JsonPath;
import com.mghostl.comfortablechanges.dao.Item;
import com.mghostl.comfortablechanges.dao.Rates;
import com.mghostl.comfortablechanges.db.RatesStorage;
import net.minidev.json.JSONArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(RatesController.class)
public class RatesControllerTest {

    @MockBean
    private RatesStorage ratesStorage;

    @Autowired
    private MockMvc mvc;

    @Test
    public void name() {
    }

    @Test
    public void shouldReturnExchangesForTwoCurrencies() throws Exception {
        String from = "USD";
        String to = "EUR";
        double in = 1;
        double out = 2;
        double amount = 1000;
        String exchange = "TestExchange";
        Rates rates = new Rates().add(new Item(from, to, in, out, amount));
        Map<String, Rates> exchanges = new HashMap<>();
        exchanges.put(exchange, rates);
        given(ratesStorage.getExchanges(from, to)).willReturn(exchanges);

        mvc.perform(get("/rates")
                .param("to", to)
                .param("from", from)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(result -> {
                    String resultStr = result.getResponse().getContentAsString();
                    Map<String, Map<String, JSONArray>> root = JsonPath.parse(resultStr).json();
                    assertTrue(root.containsKey(exchange));
                    Map<String, JSONArray> items = root.get(exchange);
                    assertEquals(1, items.size());
                    JSONArray item = items.get("items");
                    assertEquals(1, item.size());
                    assertTrue(item.get(0) instanceof Map<?, ?>);
                    Map details = (Map) item.get(0);
                    assertEquals(from, details.get("from"));
                    assertEquals(to, details.get("to"));
                    assertEquals(in, details.get("in"));
                    assertEquals(out, details.get("out"));
                    assertEquals(amount, details.get("amount"));
                });
    }
}