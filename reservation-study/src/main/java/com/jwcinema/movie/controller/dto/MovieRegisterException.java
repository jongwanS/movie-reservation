package com.jwcinema.movie.controller.dto;

public class MovieRegisterException extends RuntimeException{
    public MovieRegisterException(String msg) {
        super(msg);
    }
}
