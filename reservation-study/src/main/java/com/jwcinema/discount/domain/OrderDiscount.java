package com.jwcinema.discount.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OrderDiscount {

    private DiscountId id;
    private DiscountPolicy policy;

    public long calculateDiscountPrice(int ticketCount, long screenPrice){
        return policy.apply(ticketCount, screenPrice);
    }
}
