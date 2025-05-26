package com.cob.ppa.constant;

public enum ImportRequestStatus {
    Processing(202),
    Success(200),
    Failure(500);

    private final int value;

    ImportRequestStatus(final int newValue) {
        value = newValue;
    }

    public int getValue() { return value; }
}
