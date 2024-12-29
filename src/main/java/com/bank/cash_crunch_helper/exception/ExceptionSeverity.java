package com.bank.cash_crunch_helper.exception;

public enum ExceptionSeverity {

    INFO("CCH INFO"),
    WARNING("CCH WARNING"),
    ERROR("CCH ERROR"),
    FATAL("CCH FATAL");

    private final String text;

    ExceptionSeverity(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
