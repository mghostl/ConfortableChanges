package com.mghostl.comfortablechanges.dao;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@XStreamAlias("exchange")
@Data
@RequiredArgsConstructor
public class Exchange {
    private final String name;
    private final String url;
    private String ref;
}