package com.mghostl.comfortablechanges.rest;

import com.mghostl.comfortablechanges.dao.Currency;
import com.mghostl.comfortablechanges.dao.Rates;
import com.mghostl.comfortablechanges.db.RatesStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
public class RatesController {

    private final RatesStorage ratesStorage;
    private final Logger logger = LoggerFactory.getLogger(RatesController.class);

    @Autowired
    public RatesController(RatesStorage ratesStorage) {
        this.ratesStorage = ratesStorage;
    }

    @GetMapping("/rates")
    public Rates[] rates(@RequestParam(value = "from") String from,
                         @RequestParam(value = "to") String to){

        if(from == null || to == null) {
            throw new NullPointerException("Please provide currencyFrom and currencyTo! from = " + from + " to = " + to);
        }
        logger.info("request for getRates({}, {})", from, to);
        return ratesStorage.getExchanges(from, to);
    }

    @GetMapping("/currencies")
    public Currency[] currencies() {
        logger.info("request for getCurrencies");
        Currency[] result = ratesStorage.getCurrencies();
        logger.debug("currencies: ");
        logCurrencies(result);
        return result;
    }

    @GetMapping("/from")
    public Currency[] from() {
        logger.info("request for getFrom");
        Currency[] result = ratesStorage.getFrom();
        logger.debug("from: ");
        logCurrencies(result);
        return result;
    }

    @GetMapping("/to")
    public Currency[] to(@RequestParam(value = "from") String from) {
        logger.info("request for getTo");
        Currency[] result = ratesStorage.getTo(from);
        logger.debug("to: ");
        logCurrencies(result);
        return result;
    }


    private void logCurrencies(Currency[] currencies) {
        Arrays.stream(currencies)
                .map(Currency::getName)
                .forEach(currency -> logger.debug(" {} ", currency));
    }
}