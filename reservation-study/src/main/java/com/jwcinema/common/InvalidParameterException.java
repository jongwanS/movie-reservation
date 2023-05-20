package com.jwcinema.common;

public class InvalidParameterException extends RuntimeException{
    public InvalidParameterException(String msg) {
        super(msg);
    }
}