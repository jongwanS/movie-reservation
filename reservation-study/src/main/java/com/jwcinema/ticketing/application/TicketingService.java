package com.jwcinema.ticketing.application;

import com.jwcinema.discount.domain.OrderDiscountEntity;
import com.jwcinema.discount.infra.OrderDiscountEntityRepository;
import com.jwcinema.screen.domain.Screen;
import com.jwcinema.screen.infra.ScreenEntityRepository;
import com.jwcinema.ticketing.controller.dto.TicketingRequest;
import com.jwcinema.ticketing.domain.DiscountCalculationService;
import com.jwcinema.ticketing.domain.ScreenScheduleNotExistException;
import com.jwcinema.ticketing.domain.Ticketing;
import com.jwcinema.ticketing.infra.TicketingEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TicketingService {
    private final TicketingEntityRepository ticketingEntityRepository;
    private final ScreenEntityRepository screenEntityRepository;
    private final OrderDiscountEntityRepository orderDiscountEntityRepository;
    private final DiscountCalculationService discountCalculationService;
    @Transactional
    public Ticketing reserve(TicketingRequest ticketingRequest){
        Screen screen = screenEntityRepository.selectMovieTitleAndStartAtAndEndAt(
                        ticketingRequest.getMovieTitle(),ticketingRequest.getStartAt(),ticketingRequest.getEndAt())
                .orElseThrow(() -> new ScreenScheduleNotExistException(ticketingRequest.getMovieTitle() +" 로 등록된 상영정보 없습니다"));

        int dayOfOrder = screenEntityRepository.findDayOfOrder(screen.getMovieTitle(), screen.getStartAt(),screen.getEndAt(), screen.getStartAt().toLocalDate());
        Optional<OrderDiscountEntity> orderDiscountEntity = orderDiscountEntityRepository.findByDiscountDateAndDayOfOrder(screen.getStartAt().toLocalDate(), dayOfOrder);

        Ticketing ticketing = Ticketing.createTicket(
                ticketingRequest.getPhoneNumber()
                , ticketingRequest.getTicketCount());
        ticketing.calculateDiscountedPrice(discountCalculationService
                , screen.getPrice()
                , orderDiscountEntity.map(OrderDiscountEntity::toOrderDiscount).orElse(Optional.empty())
        );

        ticketingEntityRepository.save(ticketing.toEntity());

        return ticketing;
    }

    @Transactional
    public void cancel(String ticketingId) {
        Ticketing ticketing = ticketingEntityRepository.selectTickingById(ticketingId)
                .orElseThrow(() -> new RuntimeException(ticketingId + " 로 등록된 티켓정보가 없습니다"));
//        ticketing.cancel();
    }
}
