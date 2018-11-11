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

    public static Parameter of(String param) {
        switch (param.toLowerCase()) {
            case "manual":
                return MANUAL;
            case "juridical":
                return  JURIDICAL;
            case "verifying":
                return VERIFYING;
            case "floating":
                return FLOATING;
            case "otherin":
                return OTHERIN;
            case "otherout":
                return OTHEROUT;
            default:
                throw new IllegalArgumentException("Couldn't parse string" + param + " as Parameter enum");
        }
    }

}
