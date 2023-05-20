package com.jwcinema.ticketing.application;

import com.jwcinema.discount.domain.DiscountId;
import com.jwcinema.discount.domain.DiscountPolicy;
import com.jwcinema.discount.domain.OrderDiscount;
import com.jwcinema.discount.infra.OrderDiscountEntityRepository;
import com.jwcinema.screen.domain.Screen;
import com.jwcinema.screen.infra.ScreenEntityRepository;
import com.jwcinema.ticketing.controller.dto.TicketingRequest;
import com.jwcinema.ticketing.domain.*;
import com.jwcinema.ticketing.infra.TicketingEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketingServiceTest {
    @Mock
    private ScreenEntityRepository screenEntityRepository;
    @Mock
    private TicketingEntityRepository ticketingEntityRepository;

    @Mock
    private OrderDiscountEntityRepository orderDiscountEntityRepository;

    @InjectMocks
    private TicketingService ticketingService;

    @Nested
    @DisplayName("티케팅")
    class TicketReserve {

        @Test
        @DisplayName("티케팅 - 실패(상영테이블에 등록된 영화가 없다 -> ScreenScheduleNotExistException)")
        void ticketing_screen_missing() {
            // given
            TicketingRequest ticketingRequest = TicketingRequest.builder()
                    .movieTitle("리바운드")
                    .phoneNumber("01012341234")
                    .startAt(LocalDateTime.now())
                    .endAt(LocalDateTime.now())
                    .ticketCount(3)
                    .build();

            when(screenEntityRepository.selectMovieTitleAndStartAtAndEndAt(any(), any(), any()))
                    .thenThrow(new ScreenScheduleNotExistException());

            // when, then
            assertThrows(ScreenScheduleNotExistException.class, () -> ticketingService.reserve(ticketingRequest));
            verify(screenEntityRepository, times(1)).selectMovieTitleAndStartAtAndEndAt(any(), any(), any());
        }

        @Test
        @DisplayName("티케팅 - 성공(티켓 가격 : 1000원, 티켓예매수 3개, 할인X)")
        void ticketing_success_without_discount() {
            double expectedFinalPrice = 3000L;
            double expectedDiscountPrice = 0L;
            // given
            TicketingRequest ticketingRequest = TicketingRequest.builder()
                    .movieTitle("리바운드")
                    .phoneNumber("01012341234")
                    .startAt(LocalDateTime.now())
                    .endAt(LocalDateTime.now())
                    .ticketCount(3)
                    .build();

            //상영
            Screen screen = Screen.builder()
                    .movieTitle(ticketingRequest.getMovieTitle())
                    .startAt(ticketingRequest.getStartAt())
                    .endAt(ticketingRequest.getEndAt())
                    .price(1000L)
                    .build();
            TicketingEntity ticketingEntity = TicketingEntity.builder()
                    .ticketId(LocalDateTime.now() + "_" + ticketingRequest.getPhoneNumber())
                    .ticketCount(ticketingRequest.getTicketCount())
                    .status(Status.PENDING)
                    .paymentPrice(screen.getPrice() * ticketingRequest.getTicketCount())
                    .discountPrice(0L)
                    .build();
            when(screenEntityRepository.selectMovieTitleAndStartAtAndEndAt(any(), any(), any()))
                    .thenReturn(Optional.ofNullable(screen));
            when(orderDiscountEntityRepository.findByDiscountDateAndDayOfOrder(any(), any()))
                    .thenReturn(Optional.empty());
            when(ticketingEntityRepository.save(any()))
                    .thenReturn(ticketingEntity);
            Ticketing ticketing = ticketingService.reserve(ticketingRequest);

            Assertions.assertAll(
                    () -> assertEquals(expectedFinalPrice, ticketing.getPaymentPrice()),
                    () -> assertEquals(expectedDiscountPrice, ticketing.getDiscountPrice())
            );
        }

        @Test
        @DisplayName("티케팅 - 성공(티켓 가격 : 1000원, 티켓예매수 3개, 티켓 1장당 정액 500원 할인)")
        void ticketing_success_fix_discount() {
            double expectedFinalPrice = 1500L;
            double expectedDiscountPrice = 1500L;
            // given
            TicketingRequest ticketingRequest = TicketingRequest.builder()
                    .movieTitle("리바운드")
                    .phoneNumber("01012341234")
                    .startAt(LocalDateTime.now())
                    .endAt(LocalDateTime.now())
                    .ticketCount(3)
                    .build();

            //상영
            Screen screen = Screen.builder()
                    .movieTitle(ticketingRequest.getMovieTitle())
                    .startAt(ticketingRequest.getStartAt())
                    .endAt(ticketingRequest.getEndAt())
                    .price(1000L)
                    .dayOfOrder(1)
                    .build();

            //할인
            Optional<OrderDiscount> orderDiscount = Optional.ofNullable(OrderDiscount.builder()
                    .id(new DiscountId(screen.getDayOfOrder(), screen.getStartAt().toLocalDate()))
                    .policy(
                            DiscountPolicy.builder()
                                    .type(DiscountType.FIX.getValue())
                                    .price(500L)
                                    .build()
                    )
                    .build());

            TicketingEntity ticketingEntity = TicketingEntity.builder()
                    .ticketId(LocalDateTime.now() + "_" + ticketingRequest.getPhoneNumber())
                    .ticketCount(ticketingRequest.getTicketCount())
                    .status(Status.PENDING)
                    .paymentPrice(expectedFinalPrice)
                    .discountPrice(expectedDiscountPrice)
                    .build();

            when(screenEntityRepository.selectMovieTitleAndStartAtAndEndAt(any(), any(), any()))
                    .thenReturn(Optional.ofNullable(screen));
            when(orderDiscountEntityRepository.findByDiscountDateAndDayOfOrder(any(), any()))
                    .thenReturn(orderDiscount);
            when(ticketingEntityRepository.save(any()))
                    .thenReturn(ticketingEntity);
            Ticketing ticketing = ticketingService.reserve(ticketingRequest);

            Assertions.assertAll(
                    () -> assertEquals(expectedFinalPrice, ticketing.getPaymentPrice()),
                    () -> assertEquals(expectedDiscountPrice, ticketing.getDiscountPrice())
            );
        }

        @Test
        @DisplayName("티케팅 - 성공(티켓 가격 : 1000원, 티켓예매수 3개, 티켓 1장당 정률 10% 할인)")
        void ticketing_success_rate_discount() {
            double expectedFinalPrice = 2700L;
            double expectedDiscountPrice = 300L;
            // given
            TicketingRequest ticketingRequest = TicketingRequest.builder()
                    .movieTitle("리바운드")
                    .phoneNumber("01012341234")
                    .startAt(LocalDateTime.now())
                    .endAt(LocalDateTime.now())
                    .ticketCount(3)
                    .build();

            //상영
            Screen screen = Screen.builder()
                    .movieTitle(ticketingRequest.getMovieTitle())
                    .startAt(ticketingRequest.getStartAt())
                    .endAt(ticketingRequest.getEndAt())
                    .price(1000L)
                    .dayOfOrder(1)
                    .build();

            //할인
            Optional<OrderDiscount> orderDiscount = Optional.ofNullable(OrderDiscount.builder()
                    .id(new DiscountId(screen.getDayOfOrder(), screen.getStartAt().toLocalDate()))
                    .policy(
                            DiscountPolicy.builder()
                                    .type(DiscountType.RATE.getValue())
                                    .rate(10)
                                    .build()
                    )
                    .build());

            TicketingEntity ticketingEntity = TicketingEntity.builder()
                    .ticketId(LocalDateTime.now() + "_" + ticketingRequest.getPhoneNumber())
                    .ticketCount(ticketingRequest.getTicketCount())
                    .status(Status.PENDING)
                    .paymentPrice(expectedFinalPrice)
                    .discountPrice(expectedDiscountPrice)
                    .build();

            when(screenEntityRepository.selectMovieTitleAndStartAtAndEndAt(any(), any(), any()))
                    .thenReturn(Optional.ofNullable(screen));
            when(orderDiscountEntityRepository.findByDiscountDateAndDayOfOrder(any(), any()))
                    .thenReturn(orderDiscount);
            when(ticketingEntityRepository.save(any()))
                    .thenReturn(ticketingEntity);
            Ticketing ticketing = ticketingService.reserve(ticketingRequest);

            Assertions.assertAll(
                    () -> assertEquals(expectedFinalPrice, ticketing.getPaymentPrice()),
                    () -> assertEquals(expectedDiscountPrice, ticketing.getDiscountPrice())
            );
        }

        @Test
        @DisplayName("티케팅 - 성공(티켓 가격 : 200원, 티켓예매수 3개, 티켓 1장당 정률 1% 할인)")
        void ticketing_success_rate_discount_원절사() {
            double expectedFinalPrice = 590L;
            double expectedDiscountPrice = 10L;
            // given
            TicketingRequest ticketingRequest = TicketingRequest.builder()
                    .movieTitle("리바운드")
                    .phoneNumber("01012341234")
                    .startAt(LocalDateTime.now())
                    .endAt(LocalDateTime.now())
                    .ticketCount(3)
                    .build();

            //상영
            Screen screen = Screen.builder()
                    .movieTitle(ticketingRequest.getMovieTitle())
                    .startAt(ticketingRequest.getStartAt())
                    .endAt(ticketingRequest.getEndAt())
                    .price(200L)
                    .dayOfOrder(1)
                    .build();

            //할인
            Optional<OrderDiscount> orderDiscount = Optional.ofNullable(OrderDiscount.builder()
                    .id(new DiscountId(screen.getDayOfOrder(), screen.getStartAt().toLocalDate()))
                    .policy(
                            DiscountPolicy.builder()
                                    .type(DiscountType.RATE.getValue())
                                    .rate(1)
                                    .build()
                    )
                    .build());

            TicketingEntity ticketingEntity = TicketingEntity.builder()
                    .ticketId(LocalDateTime.now() + "_" + ticketingRequest.getPhoneNumber())
                    .ticketCount(ticketingRequest.getTicketCount())
                    .status(Status.PENDING)
                    .paymentPrice(expectedFinalPrice)
                    .discountPrice(expectedDiscountPrice)
                    .build();

            when(screenEntityRepository.selectMovieTitleAndStartAtAndEndAt(any(), any(), any()))
                    .thenReturn(Optional.ofNullable(screen));
            when(orderDiscountEntityRepository.findByDiscountDateAndDayOfOrder(any(), any()))
                    .thenReturn(orderDiscount);
            when(ticketingEntityRepository.save(any()))
                    .thenReturn(ticketingEntity);
            Ticketing ticketing = ticketingService.reserve(ticketingRequest);

            Assertions.assertAll(
                    () -> assertEquals(expectedFinalPrice, ticketing.getPaymentPrice()),
                    () -> assertEquals(expectedDiscountPrice, ticketing.getDiscountPrice())
            );
        }

        @Test
        @DisplayName("티케팅 - 성공(티켓 최소금액 100원 / 티켓 가격 : 1000원, 티켓예매수 3개, 티켓 1장당 정률 99% 할인)")
        void ticketing_success_maximum_discount() {
            double expectedFinalPrice = 100L;
            double expectedDiscountPrice = 2900L;
            // given
            TicketingRequest ticketingRequest = TicketingRequest.builder()
                    .movieTitle("리바운드")
                    .phoneNumber("01012341234")
                    .startAt(LocalDateTime.now())
                    .endAt(LocalDateTime.now())
                    .ticketCount(3)
                    .build();

            //상영
            Screen screen = Screen.builder()
                    .movieTitle(ticketingRequest.getMovieTitle())
                    .startAt(ticketingRequest.getStartAt())
                    .endAt(ticketingRequest.getEndAt())
                    .price(1000L)
                    .dayOfOrder(1)
                    .build();

            //할인
            Optional<OrderDiscount> orderDiscount = Optional.ofNullable(OrderDiscount.builder()
                    .id(new DiscountId(screen.getDayOfOrder(), screen.getStartAt().toLocalDate()))
                    .policy(
                            DiscountPolicy.builder()
                                    .type(DiscountType.RATE.getValue())
                                    .rate(99)
                                    .build()
                    )
                    .build());

            TicketingEntity ticketingEntity = TicketingEntity.builder()
                    .ticketId(LocalDateTime.now() + "_" + ticketingRequest.getPhoneNumber())
                    .ticketCount(ticketingRequest.getTicketCount())
                    .status(Status.PENDING)
                    .paymentPrice(expectedFinalPrice)
                    .discountPrice(expectedDiscountPrice)
                    .build();

            when(screenEntityRepository.selectMovieTitleAndStartAtAndEndAt(any(), any(), any()))
                    .thenReturn(Optional.ofNullable(screen));
            when(orderDiscountEntityRepository.findByDiscountDateAndDayOfOrder(any(), any()))
                    .thenReturn(orderDiscount);
            when(ticketingEntityRepository.save(any()))
                    .thenReturn(ticketingEntity);
            Ticketing ticketing = ticketingService.reserve(ticketingRequest);

            Assertions.assertAll(
                    () -> assertEquals(expectedFinalPrice, ticketing.getPaymentPrice()),
                    () -> assertEquals(expectedDiscountPrice, ticketing.getDiscountPrice())
            );
        }
    }

//    @Nested
//    @DisplayName("티케팅취소")
//    class TicketCancel {
//        @Test
//        @DisplayName("티케팅취소 - 실패(티케팅 이력이 없다 -> RuntimeException)")
//        void ticketing_cancel_fail_history_missing() {
//            // given
//            TicketingCancelRequest ticketingCancelRequest = TicketingCancelRequest.builder()
//                    .ticketingId(1L)
//                    .build();
//            when(ticketingEntityRepository.findById(any())).thenThrow(new RuntimeException());
//
//            // when, then
//            assertThrows(RuntimeException.class, () -> ticketingService.cancel(ticketingCancelRequest.getTicketingId()));
//            verify(ticketingEntityRepository, times(1)).findById(any());
//        }
//
//        @Test
//        @DisplayName("티케팅취소 - 성공")
//        void ticketing_cancel_success(){
//            // given
//            TicketingCancelRequest ticketingCancelRequest = TicketingCancelRequest.builder()
//                    .ticketingId(1L)
//                    .build();
//
//            TicketingEntity ticketing = TicketingEntity.builder()
//                    .id(1L)
//                    .ticketCount(5)
//                    .build();
//
//            // when
//            when(ticketingEntityRepository.findById(ticketingCancelRequest.getTicketingId())).thenReturn(Optional.ofNullable(ticketing));
//            ticketingService.cancel(ticketingCancelRequest.getTicketingId());
//
//            //then
//            verify(ticketingEntityRepository,times(1)).findById(any());
//        }
//    }
}