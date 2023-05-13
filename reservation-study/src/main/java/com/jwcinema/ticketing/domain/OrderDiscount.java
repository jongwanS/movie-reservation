package com.jwcinema.ticketing.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class OrderDiscount {

    private Integer dayOfOrder;
    private LocalDate date;
    private String type;
    private Integer rate;
    private Long price;

    public boolean isEmpty() {
        return dayOfOrder == null;
    }
}