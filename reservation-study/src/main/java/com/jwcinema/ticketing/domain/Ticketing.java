package com.jwcinema.ticketing.domain;


import lombok.Builder;
import lombok.Getter;

@Getter
public class Ticketing {

    private String id;
    private Integer ticketCount;
    private Status status;
    private Long price;
    private Long discountPrice;

    @Builder
    public Ticketing(String id, Integer ticketCount, Status status, Long price, Long discountPrice) {
        this.id = id;
        this.ticketCount = ticketCount;
        this.status = status;
        this.price = price;
        this.discountPrice = discountPrice;
    }
}
