package com.jwcinema.ticketing.application;

import com.jwcinema.discount.domain.OrderDiscountEntity;
import com.jwcinema.discount.infra.OrderDiscountEntityRepository;
import com.jwcinema.movie.domain.MovieEntity;
import com.jwcinema.movie.infra.MovieEntityRepository;
import com.jwcinema.screen.domain.ScreenEntity;
import com.jwcinema.screen.domain.ScreenRegisterException;
import com.jwcinema.screen.infra.ScreenEntityRepository;
import com.jwcinema.ticketing.controller.dto.TicketingRequest;
import com.jwcinema.ticketing.domain.*;
import com.jwcinema.ticketing.infra.TicketingEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketingService {
    private final ScreenEntityRepository screenEntityRepository;
    private final TicketingEntityRepository ticketingEntityRepository;
    private final OrderDiscountEntityRepository orderDiscountEntityRepository;
    private final MovieEntityRepository movieEntityRepository;

    @Transactional
    public Ticketing reserve(TicketingRequest ticketingRequest){
        //영화 식별자로 상영관 정보를 찾는다.
        MovieEntity movie = movieEntityRepository.findByTitle(ticketingRequest.getMovieTitle())
                .orElseThrow(() -> new ScreenRegisterException(ticketingRequest.getMovieTitle()+" 로 등록된 영화가 없습니당 "));
        //상영 식별자로 상영관 정보를 찾는다.
        ScreenEntity screenEntity =screenEntityRepository.findByMovieIdAndStartAtAndEndAt(
                        movie.getId(),ticketingRequest.getStartAt(),ticketingRequest.getEndAt())
                .orElseThrow(() -> new ScreenScheduleNotExistException(ticketingRequest.getMovieTitle() +" 로 등록된 상영정보 없습니다"));

        //각 도메인 별로 이런식으로 새로 만들어야 하나??
        Screen screen = Screen.builder()
                .movieTitle(ticketingRequest.getMovieTitle())
                .startAt(ticketingRequest.getStartAt())
                .endAt(ticketingRequest.getEndAt())
                .price(screenEntity.getPrice())
                .build();

        //영화 시작 시간을 기준으로 해당 날짜의 영화 전체 목록을 가져온다.
        List<ScreenEntity> screenEntities = screenEntityRepository
                .findAllByStartAtBetween(screen.getStartAt().toLocalDate(),
                        screen.getStartAt().toLocalDate().plusDays(1));

        //티켓팅 하려는 오늘의 영화의 순번을 구한다.
        Integer dayOfOrder = getTodayMovieOrder(screenEntities, screen);

        //순번 할인정책을 조회한다.
        OrderDiscount orderDiscount = getOrderDiscount(dayOfOrder, screen.getStartAt());

        //티켓을 할인해주는 객체를 생성한다.
        TicketDiscount ticketDiscount = TicketDiscount.builder()
                .orderDiscount(orderDiscount)
                .ticketPrice(screen.getPrice())
                .ticketCount(ticketingRequest.getTicketCount())
                .build();

        //티켓팅을 진행한다.
        TicketingEntity ticketingEntity = ticketingEntityRepository.save(
                TicketingEntity.builder()
                        .ticketId(LocalDateTime.now() +"_"+ ticketingRequest.getPhoneNumber())
                        .ticketCount(ticketingRequest.getTicketCount())
                        .status(Status.PENDING)
                        .price(ticketDiscount.calculateFinalPrice())
                        .discountPrice(ticketDiscount.calculateFinalDiscountPrice())
                        .build());

        //티켓생성완료
        return Ticketing.builder()
                .id(ticketingEntity.getTicketId())
                .ticketCount(ticketingRequest.getTicketCount())
                .price(ticketDiscount.calculateFinalPrice())
                .discountPrice(ticketDiscount.calculateFinalDiscountPrice())
                .status(ticketingEntity.getStatus())
                .build();
    }

    /**
     * 오늘날짜의 순번 할인을 구한다.
     *
     * @param dayOfOrder
     * @param startAt
     * @return
     */
    private OrderDiscount getOrderDiscount(Integer dayOfOrder, LocalDateTime startAt) {
        Optional<OrderDiscountEntity> orderDiscountEntity = orderDiscountEntityRepository
                .findByDiscountDateAndDayOfOrder(startAt.toLocalDate(), dayOfOrder);

        if(!orderDiscountEntity.isPresent()){
            return OrderDiscount.builder().build();
        }

        OrderDiscount orderDiscount = OrderDiscount.builder()
                .dayOfOrder(orderDiscountEntity.get().getDayOfOrder())
                .date(orderDiscountEntity.get().getDiscountDate())
                .type(orderDiscountEntity.get().getDiscountPolicy().getType())
                .rate(orderDiscountEntity.get().getDiscountPolicy().getRate())
                .price(orderDiscountEntity.get().getDiscountPolicy().getPrice())
                .build();
        return orderDiscount;
    }

    /**
     * 티켓팅 하려는 영화의 순번을 구한다.
     * @param screenEntities
     * @param screen
     * @return
     */
    private Integer getTodayMovieOrder(List<ScreenEntity> screenEntities, Screen screen) {
        Integer dayOfOrder = 1;
        for (int i = 0; i< screenEntities.size(); i++){
            if(screen.getMovieTitle().equals(screen.getMovieTitle())
                && screen.getStartAt().equals(screen.getStartAt())
                && screen.getEndAt().equals(screen.getEndAt())
            ){
                return dayOfOrder+i;
            }
        }
        return 0;
    }

    @Transactional
    public void cancel(Long ticketingId) {
        TicketingEntity ticketingEntity = ticketingEntityRepository.findById(ticketingId)
                .orElseThrow(() -> new RuntimeException(ticketingId + " 로 등록된 티켓정보가 없습니다"));
        ticketingEntity.cancel();
    }
}
