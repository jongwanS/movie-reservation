package com.jwcinema.discount.domain;

import lombok.Getter;

import java.time.LocalDate;
import java.util.Objects;

@Getter
public class DiscountId {
    private Integer dayOfOrder;
    private LocalDate date;

    public DiscountId(Integer dayOfOrder, LocalDate date) {
        this.dayOfOrder = dayOfOrder;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscountId that = (DiscountId) o;
        return Objects.equals(dayOfOrder, that.dayOfOrder) && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dayOfOrder, date);
    }

    @Override
    public String toString() {
        return "DiscountId{" +
                "dayOfOrder=" + dayOfOrder +
                ", date=" + date +
                '}';
    }
}
