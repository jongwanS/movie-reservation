package com.jwcinema.ticketing.domain;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class TicketingPayEvent {
    private String ticketId;
    private double paidPrice;
    private double discountPrice;
}
