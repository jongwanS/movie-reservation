package com.jwcinema.discount.domain;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
@Builder
public class OrderDiscount {

    private Integer dayOfOrder;
    private LocalDate date;
    private DiscountPolicy policy;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDiscount discount = (OrderDiscount) o;
        return Objects.equals(dayOfOrder, discount.dayOfOrder) && Objects.equals(date, discount.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayOfOrder, date);
    }
}
