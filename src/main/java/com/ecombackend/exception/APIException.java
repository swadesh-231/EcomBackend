package com.ecombackend.exception;

public class APIException extends RuntimeException{
    public APIException(String message) {
        super(message);
    }
    public APIException() {
    }

}