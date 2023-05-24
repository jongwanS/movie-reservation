package com.jwcinema.ticketing.infra;

import com.jwcinema.ticketing.domain.TicketingPaymentEvent;
import com.jwcinema.ticketing.infra.pg.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Service
@RequiredArgsConstructor
public class TicketingPaymentEventHandler {

    private final PaymentService paymentService;

    @Async
    @TransactionalEventListener(
            classes = TicketingPaymentEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void payApprove(TicketingPaymentEvent ticketingPaymentEvent) {
        paymentService.approve(ticketingPaymentEvent);
    }
}
