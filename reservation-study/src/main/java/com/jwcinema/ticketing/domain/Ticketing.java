package com.jwcinema.ticketing.domain;


import lombok.Builder;
import lombok.Getter;

@Getter
public class Ticketing {

    private String id;
    private Integer ticketCount;
    private Status status;
    private double paymentPrice;
    private double discountPrice;
    private final Integer MIN_PAYMENT_PRICE = 100;
    @Builder
    public Ticketing(String id, Integer ticketCount, Status status, double paymentPrice, double discountPrice) {
        this.id = id;
        this.ticketCount = ticketCount;
        this.status = status;
        this.paymentPrice = guaranteeMinPrice(truncateWon(paymentPrice - discountPrice));
        this.discountPrice = paymentPrice - this.paymentPrice;
    }

    private double guaranteeMinPrice(double paymentPrice) {
        return paymentPrice < MIN_PAYMENT_PRICE
                ? MIN_PAYMENT_PRICE
                : paymentPrice;
    }

    private double truncateWon(double paymentPrice){
        return paymentPrice - (paymentPrice % 10);
    }

    public void cancel() {
        this.status = Status.CANCEL;
    }
}
