package com.mghostl.comfortablechanges.dao;

import com.mghostl.comfortablechanges.dao.Item;
import com.mghostl.comfortablechanges.dao.Parameter;
import com.mghostl.comfortablechanges.dao.Rates;
import com.mghostl.comfortablechanges.dao.RatesMarshaller;
import org.junit.Before;
import org.junit.Test;

import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class RatesMarshallerTest {

    private final static String FILE_NAME = "rates-example.xml";
    private RatesMarshaller ratesMarshaller;

    @Before
    public void init() {
        ratesMarshaller = new RatesMarshaller();
    }

    @Test
    public void shouldCreateRatesObjectFromXml(){
        Optional<Rates> result = ratesMarshaller.fromXML(Thread.currentThread().getContextClassLoader().getResource(FILE_NAME));
        assertTrue(result.isPresent());

        Rates rates = result.get();
        assertEquals(4, rates.getItems().size());
        Item item = rates.getItems().get(0);
        assertEquals("WMZ", item.getFrom());
        assertEquals("WMR", item.getTo());
        assertEquals(1.0, item.getIn());
        assertEquals(30.593562, item.getOut());
        assertEquals(572962.42, item.getAmount());
        assertEquals(new Parameter[]{Parameter.MANUAL, Parameter.JURIDICAL}, item.getParam().toArray());
    }
}
