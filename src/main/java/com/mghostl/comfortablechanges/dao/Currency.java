package com.mghostl.comfortablechanges.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class Currency {
    private String name;
    private String source;
}
