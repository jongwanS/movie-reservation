package com.jwcinema.payment.domain;

import lombok.Builder;
@Builder
public class Payment {

    private String ticketingId;
    private double paidPrice;
    private Status status;
    private static final Integer MIN_PAYMENT_PRICE = 100;

    private static double guaranteeMinPrice(double paymentPrice) {
        return paymentPrice < MIN_PAYMENT_PRICE
                ? MIN_PAYMENT_PRICE
                : paymentPrice;
    }

    private static double truncateWon(double paymentPrice){
        return paymentPrice - (paymentPrice % 10);
    }

    public static Payment createPayment(String ticketId, double paidPrice) {
        return Payment.builder()
                .ticketingId(ticketId)
                .paidPrice(guaranteeMinPrice(truncateWon(paidPrice)))
                .status(Status.COMPLETE)
                .build();
    }

    public PaymentEntity toEntity() {
        return PaymentEntity.builder()
                .ticketId(ticketingId)
                .paidPrice(paidPrice)
                .status(status)
                .build();
    }
}
