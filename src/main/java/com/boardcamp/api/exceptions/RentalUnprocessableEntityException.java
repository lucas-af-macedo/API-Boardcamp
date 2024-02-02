package com.boardcamp.api.exceptions;

public class CustomerUnprocessableEntityException extends RuntimeException{
    public CustomerUnprocessableEntityException(String message){
        super(message);
    }
}
