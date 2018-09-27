package com.mghostl.comfortablechanges.dao;

public enum Parameter {
    MANUAL,
    JURIDICAL,
    VERIFYING,
    FLOATING,
    OTHERIN,
    OTHEROUT;

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
