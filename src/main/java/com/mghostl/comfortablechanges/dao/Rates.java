package com.mghostl.comfortablechanges.dao;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@XStreamAlias("rates")
@RequiredArgsConstructor
@Data
public
class Rates {

    @XStreamImplicit
    @XStreamAlias("items")
    private List<Item> items = new ArrayList<>();

    private Exchange exchange;

    public Rates add(Item item) {
        items.add(item);
        return this;
    }

    public void addAllItems(Rates otherRates) {
        otherRates.getItems().forEach(this::add);
    }

    public Rates copyExchange(Rates otherRates) {
        this.setExchange(otherRates.getExchange());
        return this;
    }

    public void replaceAllItems(Rates otherRates) {
        if(!otherRates.getExchange().equals(exchange)) {
            return;
        }
        items = otherRates.getItems();
    }

}
