package com.jwcinema.ticketing.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketingCancelRequest {

    private Long ticketingId;

    public void validate() throws Exception {
        if(ObjectUtils.isEmpty(ticketingId)){
            throw new RuntimeException("ticketingId 티켓번호는 필수값입니다.");
        }
    }
}
