package com.jwcinema.common;

import lombok.Getter;

@Getter
public enum ErrorCode {

    MOVIE_TITLE_REQUIRED("영화명은 필수 값 입니다."),
    TICKET_COUNT_REQUIRED("티켓 갯수는 필수값입니다."),
    MOBILE_NO_REQUIRED("예매시 휴대폰번호는 필수 입니다."),
    MOVIE_START_TIME_REQUIRED("예매시 영화시작 시간은 필수 입니다."),
    MOVIE_END_TIME_REQUIRED("예매시 영화종료 시간은  입니다.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
