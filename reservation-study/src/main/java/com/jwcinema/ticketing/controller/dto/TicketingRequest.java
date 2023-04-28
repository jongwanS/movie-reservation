package com.jwcinema.ticketing.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TicketingRequest {

    private Long screenId;
    private Integer ticketCount;

    public void validate() throws Exception {
        if(ObjectUtils.isEmpty(screenId)){
            throw new RuntimeException("screenId(상영ID)는 필수값입니다.");
        }
        if(ObjectUtils.isEmpty(ticketCount) || ticketCount < 1){
            throw new RuntimeException("티켓 갯수는 필수값입니다.");
        }
    }
}
