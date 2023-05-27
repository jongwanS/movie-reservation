package com.jwcinema.ticketing.domain;


import com.jwcinema.common.event.Events;
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
        //순서가 바뀌면 데이터가 꼬임
        this.paymentPrice = guaranteeMinPrice(truncateWon(paymentPrice - discountPrice));
        this.discountPrice = paymentPrice - this.paymentPrice;
        Events.raise(new TicketingPayEvent(id, paymentPrice));
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
        verifyCompleteStatus();
        this.status = Status.CANCEL;
        Events.raise(new TicketingCancelEvent(id, paymentPrice));
    }

    private void verifyCompleteStatus() {
        if(this.status != Status.COMPLETE)
            throw new RuntimeException("취소 불가상태 입니다");
    }

    public void approve() {
        this.status = Status.COMPLETE;
    }
}
