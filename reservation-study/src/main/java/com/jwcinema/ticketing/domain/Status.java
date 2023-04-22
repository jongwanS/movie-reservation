package com.jwcinema.ticketing.domain;

import lombok.Getter;

@Getter
public enum Status {
    COMPLETE("예매성공"),
    CANCEL("예매취소");

    private String description;

    Status(String description) {
        this.description = description;
    }
}
