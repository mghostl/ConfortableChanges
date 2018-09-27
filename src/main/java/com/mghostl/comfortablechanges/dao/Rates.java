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

    Rates add(Item item) {
        items.add(item);
        return this;
    }

}
