package com.jwcinema.ticketing.infra.pg;

import com.jwcinema.ticketing.domain.Ticketing;
import com.jwcinema.ticketing.domain.TicketingCancelEvent;
import com.jwcinema.ticketing.domain.TicketingPayEvent;
import com.jwcinema.ticketing.infra.TicketingEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final TicketingEntityRepository ticketingEntityRepository;

    public void approve(TicketingPayEvent ticketingPayEvent) {
        //결제로직
        ticketingPayEvent.getId();
        ticketingPayEvent.getPaymentPrice();

        //결제성공시, 상태값 변경
        Ticketing ticketing = ticketingEntityRepository.selectTickingById(ticketingPayEvent.getId())
                .orElseThrow(() -> new RuntimeException("티켓정보가 존재하지 않습니다."));
        ticketing.approve();
    }

    public void cancel(TicketingCancelEvent ticketingCancelEvent) {
        //취소처리
        ticketingCancelEvent.getId();
        ticketingCancelEvent.getPaymentPrice();
    }
}
