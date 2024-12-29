package com.bank.cash_crunch_helper.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BusinessException extends RuntimeException {

    public BusinessException(ExceptionSeverity severity, String message) {
        super("[" + severity.getText() + "] " + message);
    }
}