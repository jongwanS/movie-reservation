package com.jwcinema.discount.domain;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Getter
@Embeddable
@NoArgsConstructor
public class DiscountId implements Serializable {
    private Integer dayOfOrder;
    private LocalDate date;

    @Builder
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
