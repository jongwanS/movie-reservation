package com.jwcinema.discount.domain;

import com.jwcinema.ticketing.domain.DiscountType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DiscountPolicy {

    private String type;
    private Integer rate;
    private Long price;

    private boolean isRateDiscount(String type) {
        return DiscountType.RATE.getValue().equals(type);
    }

    public long apply(int ticketCount, long screenPrice) {
        if(isRateDiscount(this.getType())){
            return ticketCount * (screenPrice * getRate()/100);
        }
        return ticketCount * (screenPrice-price);
    }
}
