package com.mghostl.comfortablechanges.db;

import com.mghostl.comfortablechanges.dao.Currency;
import com.mghostl.comfortablechanges.dao.Exchange;
import com.mghostl.comfortablechanges.dao.Rates;

import java.util.Optional;

public interface RatesStorage {
    void addRates(Exchange exchange, Rates rates);
    Rates[] getExchanges(String from, String to);
    Optional<Rates> getRatesForExchange(String exchangeName);
    Currency[] getCurrencies();
    Currency[] getFrom();
    Currency[] getTo(String from);
}
