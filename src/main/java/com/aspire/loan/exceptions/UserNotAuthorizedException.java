package com.aspire.loan.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.FORBIDDEN)
public class UserNotAuthorizedException extends RuntimeException{

    public UserNotAuthorizedException(String message){
        super(message);
    }
}
