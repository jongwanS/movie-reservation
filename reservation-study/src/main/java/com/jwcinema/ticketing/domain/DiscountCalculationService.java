package com.jwcinema.ticketing.domain;

import com.jwcinema.discount.domain.OrderDiscount;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DiscountCalculationService {
    public double calculateDiscountedPrice(long ticketPrice, Optional<OrderDiscount> orderDiscount) {
        return orderDiscount.map(discount -> discount.calculateDiscountPrice(ticketPrice)).orElse(0L);
    }
}
