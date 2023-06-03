package com.jwcinema.ticketing.domain;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TicketingPayEvent {
    private String ticketId;
    private double paidPrice;
    private double discountPrice;
}
