package com.jwcinema.ticketing.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Ticketing {
    private Movie movie;
    private Screen screen;
    private Integer ticketCount;
    private Status status;

    public void seatLimitValidte() throws Exception {
        if(screen.getSeatLimit() > screen.getSeatReservedCount() + this.ticketCount){
            throw new Exception("영화좌석이 가득 찼습니다.");
        }
    }
}
