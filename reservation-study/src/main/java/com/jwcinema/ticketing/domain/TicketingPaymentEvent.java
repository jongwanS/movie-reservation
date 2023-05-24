package com.jwcinema.ticketing.domain;


public class TicketingPaymentEvent {
    private String id;
    private double paymentPrice;

    public TicketingPaymentEvent(String id, double paymentPrice) {
        this.id = id;
        this.paymentPrice = paymentPrice;
    }
}
