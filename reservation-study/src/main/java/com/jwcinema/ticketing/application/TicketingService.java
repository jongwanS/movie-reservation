package com.jwcinema.ticketing.application;

import com.jwcinema.discount.domain.OrderDiscount;
import com.jwcinema.discount.infra.OrderDiscountEntityRepository;
import com.jwcinema.screen.domain.Screen;
import com.jwcinema.screen.infra.ScreenEntityRepository;
import com.jwcinema.ticketing.controller.dto.TicketingRequest;
import com.jwcinema.ticketing.domain.ScreenScheduleNotExistException;
import com.jwcinema.ticketing.domain.Status;
import com.jwcinema.ticketing.domain.Ticketing;
import com.jwcinema.ticketing.domain.TicketingEntity;
import com.jwcinema.ticketing.infra.TicketingEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketingService {
    private final ScreenEntityRepository screenEntityRepository;
    private final TicketingEntityRepository ticketingEntityRepository;
    private final OrderDiscountEntityRepository orderDiscountEntityRepository;

    @Transactional
    public Ticketing reserve(TicketingRequest ticketingRequest){
        //상영 식별자로 상영관 정보를 찾는다.
        Screen screen = screenEntityRepository.selectMovieTitleAndStartAtAndEndAt(
                        ticketingRequest.getMovieTitle(),ticketingRequest.getStartAt(),ticketingRequest.getEndAt())
                .orElseThrow(() -> new ScreenScheduleNotExistException(ticketingRequest.getMovieTitle() +" 로 등록된 상영정보 없습니다"));

        //할인을 조회한다.
        Optional<OrderDiscount> orderDiscount = orderDiscountEntityRepository.findByDiscountDateAndDayOfOrder(screen.getStartAt().toLocalDate(), screen.getDayOfOrder());

        Ticketing ticketing = Ticketing.builder()
                .ticketCount(ticketingRequest.getTicketCount())
                .id(LocalDateTime.now() +"_"+ ticketingRequest.getPhoneNumber())
                .paymentPrice(ticketingRequest.getTicketCount() * screen.getPrice())
                .discountPrice(
                        orderDiscount
                        .map(discount ->
                                discount.calculateDiscountPrice(ticketingRequest.getTicketCount(), screen.getPrice())
                        )
                        .orElse(0L)
                )
                .status(Status.PENDING)
                .build();

        ticketingEntityRepository.save(
                TicketingEntity.builder()
                        .ticketId(ticketing.getId())
                        .ticketCount(ticketingRequest.getTicketCount())
                        .status(Status.PENDING)
                        .paymentPrice(ticketing.getPaymentPrice())
                        .discountPrice(ticketing.getDiscountPrice())
                        .build());

        return ticketing;
    }

    @Transactional
    public void cancel(String ticketingId) {
        Ticketing ticketing = ticketingEntityRepository.selectTickingById(ticketingId)
                .orElseThrow(() -> new RuntimeException(ticketingId + " 로 등록된 티켓정보가 없습니다"));
        ticketing.cancel();
    }
}
