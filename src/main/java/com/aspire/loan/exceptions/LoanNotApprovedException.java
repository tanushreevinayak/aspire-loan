package com.aspire.loan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.PRECONDITION_FAILED)
public class LoanNotApprovedException extends RuntimeException{

    public LoanNotApprovedException(String message){
        super(message);
    }
}
