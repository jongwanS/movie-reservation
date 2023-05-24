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

    /**
     * 오늘날짜의 순번 할인을 구한다.
     *
     * @param dayOfOrder
     * @param startAt
     * @return
     */
//    private OrderDiscount getOrderDiscount(Integer dayOfOrder, LocalDateTime startAt) {
//        Optional<OrderDiscountEntity> orderDiscountEntity = orderDiscountEntityRepository
//                .findByDiscountDateAndDayOfOrder(startAt.toLocalDate(), dayOfOrder);
//
//        if(!orderDiscountEntity.isPresent()){
//            return OrderDiscount.builder().build();
//        }
//
//        OrderDiscount orderDiscount = OrderDiscount.builder()
//                .dayOfOrder(orderDiscountEntity.get().getDayOfOrder())
//                .date(orderDiscountEntity.get().getDiscountDate())
//                .type(orderDiscountEntity.get().getDiscountPolicy().getType())
//                .rate(orderDiscountEntity.get().getDiscountPolicy().getRate())
//                .price(orderDiscountEntity.get().getDiscountPolicy().getPrice())
//                .build();
//        return orderDiscount;
//    }
//
//    /**
//     * 티켓팅 하려는 영화의 순번을 구한다.
//     * @param screenEntities
//     * @param screen
//     * @return
//     */
//    private Integer getTodayMovieOrder(List<ScreenEntity> screenEntities, Screen screen) {
//        Integer dayOfOrder = 1;
//        for (int i = 0; i< screenEntities.size(); i++){
//            if(screen.getMovieTitle().equals(screen.getMovieTitle())
//                && screen.getStartAt().equals(screen.getStartAt())
//                && screen.getEndAt().equals(screen.getEndAt())
//            ){
//                return dayOfOrder+i;
//            }
//        }
//        return 0;
//    }
//
}
