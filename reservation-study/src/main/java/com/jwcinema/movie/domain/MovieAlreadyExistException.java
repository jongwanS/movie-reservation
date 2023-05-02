package com.jwcinema.movie.domain;

public class MovieAlreadyExistException extends RuntimeException{
    public MovieAlreadyExistException(String msg) {
        super(msg);
    }
}
