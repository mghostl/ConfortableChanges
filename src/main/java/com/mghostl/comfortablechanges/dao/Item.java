package com.mghostl.comfortablechanges.dao;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@XStreamAlias("item")
@RequiredArgsConstructor
@Data
public class Item {
    private final String from;
    private final String to;
    private final double in;
    private final double out;
    @XStreamAlias("minamount")
    private String minAmount;
    @XStreamAlias("maxamount")
    private String maxAmount;
    @XStreamAlias("minfee")
    private String minFee;
    @XStreamAlias("tofee")
    private String toFee;
    private String city;
    private final double amount;
    private Set<String> param;
}
