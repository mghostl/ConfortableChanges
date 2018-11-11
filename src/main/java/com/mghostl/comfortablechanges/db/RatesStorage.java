package com.mghostl.comfortablechanges.db;

import com.mghostl.comfortablechanges.dao.Exchange;
import com.mghostl.comfortablechanges.dao.Rates;

public interface RatesStorage {
    void addRates(Exchange exchange, Rates rates);
    Rates[] getExchanges(String from, String to);
}
