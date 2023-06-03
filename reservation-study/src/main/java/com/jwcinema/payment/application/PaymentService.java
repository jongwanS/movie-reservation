package com.jwcinema.payment.application;

import com.jwcinema.discount.domain.OrderDiscount;
import com.jwcinema.discount.domain.OrderDiscountEntity;
import com.jwcinema.discount.infra.OrderDiscountEntityRepository;
import com.jwcinema.payment.domain.Payment;
import com.jwcinema.payment.infra.PaymentEntityRepository;
import com.jwcinema.screen.infra.ScreenEntityRepository;
import com.jwcinema.ticketing.domain.TicketingCancelEvent;
import com.jwcinema.ticketing.domain.TicketingPayEvent;
import com.jwcinema.ticketing.infra.TicketingEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentEntityRepository paymentEntityRepository;

    public void approve(TicketingPayEvent ticketingPayEvent) {
        Payment payment = Payment.createPayment(ticketingPayEvent.getTicketId(), ticketingPayEvent.getPaidPrice());
        //결제승인 성공시 저장
        paymentEntityRepository.save(payment.toEntity());
    }

    public void cancel(TicketingCancelEvent ticketingCancelEvent) {
        //취소처리
        ticketingCancelEvent.getId();
        ticketingCancelEvent.getPaymentPrice();
    }
}
