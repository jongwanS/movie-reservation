package com.jwcinema.ticketing.domain;

public class ScreenScheduleNotExistException extends RuntimeException{
    public ScreenScheduleNotExistException(String msg) {
        super(msg);
    }
    public ScreenScheduleNotExistException() {
        super();
    }
}
