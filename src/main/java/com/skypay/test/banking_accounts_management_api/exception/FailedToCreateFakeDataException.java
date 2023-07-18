package com.skypay.test.banking_accounts_management_api.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NO_CONTENT)
public class FailedToCreateFakeDataException extends RuntimeException{
    public FailedToCreateFakeDataException(String message) {
        super(message);
    }
}
