package com.aspire.loan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class LoanNotFoundException extends RuntimeException{

    public LoanNotFoundException(String message){
        super(message);
    }
}
