package com.jwcinema.ticketing.controller;

import lombok.Getter;
import org.springframework.util.ObjectUtils;

@Getter
public class TicketingReserveRequest {

    private Long screenId;
    private Integer ticketCount;

    public void validate() throws Exception {
        if(ObjectUtils.isEmpty(screenId)){
            throw new Exception("screenId(상영ID)는 필수값입니다.");
        }
        if(ObjectUtils.isEmpty(ticketCount)){
            throw new Exception("티켓 갯수는 필수값입니다.");
        }
    }
}
