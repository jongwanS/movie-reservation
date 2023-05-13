package com.jwcinema.ticketing.domain;

import lombok.Getter;

@Getter
public enum Status {
    PENDING("결제대기"),
    COMPLETE("예매성공"),
    CANCEL("예매취소");

    private String description;

    Status(String description) {
        this.description = description;
    }
}
