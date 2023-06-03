package com.jwcinema.ticketing.domain;


import com.jwcinema.common.ErrorCode;
import com.jwcinema.common.InvalidParameterException;
import com.jwcinema.common.event.Events;
import com.jwcinema.discount.domain.OrderDiscount;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.Optional;

@Getter
@Builder
public class Ticketing {

    private String id;
    private Integer ticketCount;

    public static Ticketing createTicket(String phoneNumber, int ticketCount) {
        validate(phoneNumber, ticketCount);
        String id = LocalDateTime.now() +"_"+ phoneNumber;
        return Ticketing.builder()
                .id(id)
                .ticketCount(ticketCount)
                .build();
    }

    private static void validate(String movieTitle, int ticketCount) {
        if(ObjectUtils.isEmpty(movieTitle)){
            throw new InvalidParameterException(ErrorCode.MOVIE_TITLE_REQUIRED.getMessage());
        }
        if(ObjectUtils.isEmpty(ticketCount) || ticketCount < 1){
            throw new InvalidParameterException(ErrorCode.TICKET_COUNT_REQUIRED.getMessage());
        }
    }

    public TicketingEntity toEntity() {
        return TicketingEntity.builder()
                .ticketId(id)
                .ticketCount(ticketCount)
                .build();
    }

    public void calculateDiscountedPrice(DiscountCalculationService discountCalculationService, long ticketPrice, Optional<OrderDiscount> orderDiscount) {
        double discountPrice = discountCalculationService.calculateDiscountedPrice(ticketPrice, orderDiscount);

        Events.raise(TicketingPayEvent.builder()
                .ticketId(this.id)
                .discountPrice(this.ticketCount*discountPrice)
                .paidPrice(this.ticketCount * (ticketPrice-discountPrice))
                .build());
    }
}
