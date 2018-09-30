package com.mghostl.comfortablechanges.db;

import com.mghostl.comfortablechanges.dao.Rates;

import java.util.Map;

public interface RatesStorage {
    void addRates(String exchange, Rates rates);
    Map<String, Rates> getExchanges(String from, String to);
}
