package com.mghostl.comfortablechanges.rest;

import com.mghostl.comfortablechanges.dao.Rates;
import com.mghostl.comfortablechanges.db.RatesStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class RatesController {

    private final RatesStorage ratesStorage;

    @Autowired
    public RatesController(RatesStorage ratesStorage) {
        this.ratesStorage = ratesStorage;
    }

    @RequestMapping("/rates")
    public Map<String, Rates> rates(@RequestParam(value = "from") String from,
                                    @RequestParam(value = "to") String to){

        if(from == null || to == null) {
            throw new NullPointerException("Please provide currencyFrom and currencyTo! from = " + from + " to = " + to);
        }
        return ratesStorage.getExchanges(from, to);
    }

}
