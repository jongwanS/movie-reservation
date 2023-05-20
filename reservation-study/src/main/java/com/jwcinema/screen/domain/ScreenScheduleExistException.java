package com.jwcinema.screen.domain;

public class ScreenScheduleExistException extends RuntimeException{
    public ScreenScheduleExistException(String msg) {
        super(msg);
    }
    public ScreenScheduleExistException() {
        super();
    }
}
