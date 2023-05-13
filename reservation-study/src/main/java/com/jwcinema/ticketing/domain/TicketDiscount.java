package com.jwcinema.ticketing.domain;

import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

@Getter
public class TicketDiscount {

    private OrderDiscount orderDiscount;
    private long ticketPrice;
    private int ticketCount;
    private long discountPrice;

    private final Integer MIN_PAYMENT_PRICE = 100;

    @Builder
    public TicketDiscount(OrderDiscount orderDiscount, Long ticketPrice, Integer ticketCount) {
        this.ticketPrice = ticketPrice;
        this.ticketCount = ticketCount;
        this.orderDiscount = orderDiscount;
        calculateDiscountPrice(orderDiscount);
    }

    /**
     * 티켓 1개당 할인가를 적용시킨다.
     * @param orderDiscount
     */
    private void calculateDiscountPrice(OrderDiscount orderDiscount) {
        if(ObjectUtils.isEmpty(orderDiscount) || orderDiscount.isEmpty()){
            return;
        }
        if(isRateDiscount(orderDiscount.getType())){
            this.discountPrice = (ticketPrice * orderDiscount.getRate()/100);
            return;
        }
        this.discountPrice = orderDiscount.getPrice();
    }

    /**
     * 티켓 최소금액은 100원이다.
     */
    public long calculateFinalPrice() {
        Long paymentPrice = (this.ticketPrice - this.discountPrice) * this.ticketCount;

        return truncateWon(paymentPrice) < MIN_PAYMENT_PRICE
                ? MIN_PAYMENT_PRICE
                : truncateWon(paymentPrice);
    }

    private long truncateWon(Long paymentPrice){
        return paymentPrice - (paymentPrice % 10);
    }

    public long calculateFinalDiscountPrice(){
        return truncateWon(ticketPrice * ticketCount  - calculateFinalPrice());
    }

    private boolean isRateDiscount(String type) {
        return DiscountType.RATE.getValue().equals(type);
    }
}
