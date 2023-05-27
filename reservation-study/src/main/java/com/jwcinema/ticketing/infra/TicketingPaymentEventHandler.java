package com.jwcinema.ticketing.infra;

import com.jwcinema.ticketing.domain.TicketingCancelEvent;
import com.jwcinema.ticketing.domain.TicketingPayEvent;
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
            classes = TicketingPayEvent.class,
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void payApprove(TicketingPayEvent ticketingPayEvent) {
        paymentService.approve(ticketingPayEvent);
    }

    @Async
    @TransactionalEventListener(
            classes = TicketingCancelEvent.class,
            phase = TransactionPhase.BEFORE_COMMIT
    )
    public void payCancel(TicketingCancelEvent TicketingCancelEvent) {
        paymentService.cancel(TicketingCancelEvent);
    }
}
