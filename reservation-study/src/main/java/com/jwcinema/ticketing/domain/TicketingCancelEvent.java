package com.jwcinema.ticketing.domain;

public class TicketingCancelEvent {
    private String id;
    private double paymentPrice;

    public TicketingCancelEvent(String id, double paymentPrice) {
        this.id = id;
        this.paymentPrice = paymentPrice;
    }

    public String getId() {
        return id;
    }

    public double getPaymentPrice() {
        return paymentPrice;
    }
}
