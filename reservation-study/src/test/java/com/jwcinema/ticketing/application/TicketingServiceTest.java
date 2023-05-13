package com.jwcinema.ticketing.application;

import com.jwcinema.discount.controller.dto.DiscountPolicyRequest;
import com.jwcinema.discount.controller.dto.DiscountType;
import com.jwcinema.discount.domain.DiscountPolicyEntity;
import com.jwcinema.discount.domain.OrderDiscountEntity;
import com.jwcinema.discount.infra.OrderDiscountEntityRepository;
import com.jwcinema.movie.domain.MovieEntity;
import com.jwcinema.movie.infra.MovieEntityRepository;
import com.jwcinema.screen.domain.ScreenEntity;
import com.jwcinema.screen.domain.ScreenRegisterException;
import com.jwcinema.screen.infra.ScreenEntityRepository;
import com.jwcinema.ticketing.controller.dto.TicketingCancelRequest;
import com.jwcinema.ticketing.controller.dto.TicketingRequest;
import com.jwcinema.ticketing.domain.ScreenScheduleNotExistException;
import com.jwcinema.ticketing.domain.Status;
import com.jwcinema.ticketing.domain.Ticketing;
import com.jwcinema.ticketing.domain.TicketingEntity;
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
import java.util.Arrays;
import java.util.List;
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
    private MovieEntityRepository movieEntityRepository;
    @Mock
    private OrderDiscountEntityRepository orderDiscountEntityRepository;

    @InjectMocks
    private TicketingService ticketingService;

    @Nested
    @DisplayName("티케팅")
    class TicketReserve {

        @Test
        @DisplayName("티케팅 - 실패(존재하지 않는 영화 -> ScreenRegisterException)")
        void ticketing_movie_missing() {
            // given
            TicketingRequest ticketingRequest = TicketingRequest.builder()
                    .movieTitle("리바운드")
                    .phoneNumber("01012341234")
                    .startAt(LocalDateTime.now())
                    .endAt(LocalDateTime.now())
                    .ticketCount(3)
                    .build();
            when(movieEntityRepository.findByTitle(any())).thenThrow(new ScreenRegisterException());

            // when, then
            assertThrows(ScreenRegisterException.class, () -> ticketingService.reserve(ticketingRequest));
            verify(screenEntityRepository, never()).findByMovieIdAndStartAtAndEndAt(any(),any(),any());
        }

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

            MovieEntity movie = MovieEntity.builder()
                    .id(1L)
                    .title("리바운드")
                    .build();

            when(movieEntityRepository.findByTitle(any())).thenReturn(Optional.ofNullable(movie));
            when(screenEntityRepository.findByMovieIdAndStartAtAndEndAt(any(),any(),any()))
                    .thenThrow(new ScreenScheduleNotExistException());

            // when, then
            assertThrows(ScreenScheduleNotExistException.class, () -> ticketingService.reserve(ticketingRequest));
            verify(movieEntityRepository, times(1)).findByTitle(any());
            verify(screenEntityRepository, times(1)).findByMovieIdAndStartAtAndEndAt(any(),any(),any());
        }

        @Test
        @DisplayName("티케팅 - 성공(티켓 가격 : 1000원, 티켓예매수 3개, 할인X)")
        void ticketing_success_without_discount() {
            Long expectedFinalPrice = 3000L;
            Long expectedDiscountPrice = 0L;
            // given
            TicketingRequest ticketingRequest = TicketingRequest.builder()
                    .movieTitle("리바운드")
                    .phoneNumber("01012341234")
                    .startAt(LocalDateTime.now())
                    .endAt(LocalDateTime.now())
                    .ticketCount(3)
                    .build();
            //영화
            MovieEntity movieEntity = MovieEntity.builder()
                    .id(1L)
                    .title(ticketingRequest.getMovieTitle())
                    .playtime(123)
                    .description("하이")
                    .insertDate(LocalDateTime.now())
                    .build();

            //상영
            ScreenEntity screenEntity = ScreenEntity.builder()
                    .movieId(movieEntity.getId())
                    .price(1000L)
                    .startAt(ticketingRequest.getStartAt())
                    .endAt(ticketingRequest.getStartAt().plusMinutes(movieEntity.getPlaytime()))
                    .build();

            OrderDiscountEntity savedOrderDiscount = OrderDiscountEntity.builder()
                    .build();

            List<ScreenEntity> screenEntities = Arrays.asList(
                    ScreenEntity.builder()
                            .movieId(movieEntity.getId()).startAt(screenEntity.getStartAt()).endAt(screenEntity.getEndAt())
                            .price(screenEntity.getPrice())
                            .build()
            );

            TicketingEntity ticketingEntity =TicketingEntity.builder()
                    .ticketId(LocalDateTime.now() +"_"+ ticketingRequest.getPhoneNumber())
                    .ticketCount(ticketingRequest.getTicketCount())
                    .status(Status.PENDING)
                    .price(screenEntity.getPrice()* ticketingRequest.getTicketCount())
                    .discountPrice(0L)
                    .build();

            when(movieEntityRepository.findByTitle(any())).thenReturn(Optional.ofNullable(movieEntity));
            when(screenEntityRepository.findByMovieIdAndStartAtAndEndAt(any(),any(),any()))
                    .thenReturn(Optional.ofNullable(screenEntity));
            when(screenEntityRepository.findAllByStartAtBetween(any(),any()))
                    .thenReturn(screenEntities);
            when(orderDiscountEntityRepository.findByDiscountDateAndDayOfOrder(any(),any()))
                    .thenReturn(Optional.empty());
            when(ticketingEntityRepository.save(any()))
                    .thenReturn(ticketingEntity);
            Ticketing ticketing = ticketingService.reserve(ticketingRequest);

            Assertions.assertAll(
                () -> assertEquals(expectedFinalPrice,ticketing.getPrice()),
                () -> assertEquals(expectedDiscountPrice,ticketing.getDiscountPrice())
            );
        }

        @Test
        @DisplayName("티케팅 - 성공(티켓 가격 : 1000원, 티켓예매수 3개, 티켓 1장당 정액 500원 할인)")
        void ticketing_success_fix_discount() {
            Long expectedFinalPrice = 1500L;
            Long expectedDiscountPrice = 1500L;
            // given
            TicketingRequest ticketingRequest = TicketingRequest.builder()
                    .movieTitle("리바운드")
                    .phoneNumber("01012341234")
                    .startAt(LocalDateTime.now())
                    .endAt(LocalDateTime.now())
                    .ticketCount(3)
                    .build();
            //영화
            MovieEntity movieEntity = MovieEntity.builder()
                    .id(1L)
                    .title(ticketingRequest.getMovieTitle())
                    .playtime(123)
                    .description("하이")
                    .insertDate(LocalDateTime.now())
                    .build();

            //상영
            ScreenEntity screenEntity = ScreenEntity.builder()
                    .movieId(movieEntity.getId())
                    .price(1000L)
                    .startAt(ticketingRequest.getStartAt())
                    .endAt(ticketingRequest.getStartAt().plusMinutes(movieEntity.getPlaytime()))
                    .build();

            List<ScreenEntity> screenEntities = Arrays.asList(
                    ScreenEntity.builder()
                            .movieId(movieEntity.getId()).startAt(screenEntity.getStartAt()).endAt(screenEntity.getEndAt())
                            .price(screenEntity.getPrice())
                            .build()
            );

            OrderDiscountEntity savedOrderDiscount = OrderDiscountEntity.builder()
                    .dayOfOrder(1)
                    .discountDate(screenEntity.getStartAt().toLocalDate())
                    .discountPolicy(DiscountPolicyEntity.builder()
                            .type(DiscountType.FIX.getValue())
                            .rate(0)
                            .price(500L)
                            .build())
                    .build();

            TicketingEntity ticketingEntity =TicketingEntity.builder()
                    .ticketId(LocalDateTime.now() +"_"+ ticketingRequest.getPhoneNumber())
                    .ticketCount(ticketingRequest.getTicketCount())
                    .status(Status.PENDING)
                    .price(screenEntity.getPrice()* ticketingRequest.getTicketCount())
                    .discountPrice(expectedDiscountPrice)
                    .build();

            when(movieEntityRepository.findByTitle(any())).thenReturn(Optional.ofNullable(movieEntity));
            when(screenEntityRepository.findByMovieIdAndStartAtAndEndAt(any(),any(),any()))
                    .thenReturn(Optional.ofNullable(screenEntity));
            when(screenEntityRepository.findAllByStartAtBetween(any(),any()))
                    .thenReturn(screenEntities);
            when(orderDiscountEntityRepository.findByDiscountDateAndDayOfOrder(any(),any()))
                    .thenReturn(Optional.ofNullable(savedOrderDiscount));
            when(ticketingEntityRepository.save(any()))
                    .thenReturn(ticketingEntity);
            Ticketing ticketing = ticketingService.reserve(ticketingRequest);

            Assertions.assertAll(
                    () -> assertEquals(expectedFinalPrice,ticketing.getPrice()),
                    () -> assertEquals(expectedDiscountPrice,ticketing.getDiscountPrice())
            );
        }

        @Test
        @DisplayName("티케팅 - 성공(티켓 가격 : 1000원, 티켓예매수 3개, 티켓 1장당 정률 10% 할인)")
        void ticketing_success_rate_discount() {
            Long expectedFinalPrice = 2700L;
            Long expectedDiscountPrice = 300L;
            // given
            TicketingRequest ticketingRequest = TicketingRequest.builder()
                    .movieTitle("리바운드")
                    .phoneNumber("01012341234")
                    .startAt(LocalDateTime.now())
                    .endAt(LocalDateTime.now())
                    .ticketCount(3)
                    .build();
            //영화
            MovieEntity movieEntity = MovieEntity.builder()
                    .id(1L)
                    .title(ticketingRequest.getMovieTitle())
                    .playtime(123)
                    .description("하이")
                    .insertDate(LocalDateTime.now())
                    .build();

            //상영
            ScreenEntity screenEntity = ScreenEntity.builder()
                    .movieId(movieEntity.getId())
                    .price(1000L)
                    .startAt(ticketingRequest.getStartAt())
                    .endAt(ticketingRequest.getStartAt().plusMinutes(movieEntity.getPlaytime()))
                    .build();

            List<ScreenEntity> screenEntities = Arrays.asList(
                    ScreenEntity.builder()
                            .movieId(movieEntity.getId()).startAt(screenEntity.getStartAt()).endAt(screenEntity.getEndAt())
                            .price(screenEntity.getPrice())
                            .build()
            );

            OrderDiscountEntity savedOrderDiscount = OrderDiscountEntity.builder()
                    .dayOfOrder(1)
                    .discountDate(screenEntity.getStartAt().toLocalDate())
                    .discountPolicy(DiscountPolicyEntity.builder()
                            .type(DiscountType.RATE.getValue())
                            .rate(10)
                            .price(0L)
                            .build())
                    .build();

            TicketingEntity ticketingEntity =TicketingEntity.builder()
                    .ticketId(LocalDateTime.now() +"_"+ ticketingRequest.getPhoneNumber())
                    .ticketCount(ticketingRequest.getTicketCount())
                    .status(Status.PENDING)
                    .price(screenEntity.getPrice()* ticketingRequest.getTicketCount())
                    .discountPrice(expectedDiscountPrice)
                    .build();

            when(movieEntityRepository.findByTitle(any())).thenReturn(Optional.ofNullable(movieEntity));
            when(screenEntityRepository.findByMovieIdAndStartAtAndEndAt(any(),any(),any()))
                    .thenReturn(Optional.ofNullable(screenEntity));
            when(screenEntityRepository.findAllByStartAtBetween(any(),any()))
                    .thenReturn(screenEntities);
            when(orderDiscountEntityRepository.findByDiscountDateAndDayOfOrder(any(),any()))
                    .thenReturn(Optional.ofNullable(savedOrderDiscount));
            when(ticketingEntityRepository.save(any()))
                    .thenReturn(ticketingEntity);
            Ticketing ticketing = ticketingService.reserve(ticketingRequest);

            Assertions.assertAll(
                    () -> assertEquals(expectedFinalPrice,ticketing.getPrice()),
                    () -> assertEquals(expectedDiscountPrice,ticketing.getDiscountPrice())
            );
        }

        @Test
        @DisplayName("티케팅 - 성공(티켓 가격 : 123원, 티켓예매수 3개, 티켓 1장당 정률 1% 할인)")
        void ticketing_success_rate_discount_원절사() {
            Long expectedFinalPrice = 360L;
            Long expectedDiscountPrice = 0L;
            // given
            TicketingRequest ticketingRequest = TicketingRequest.builder()
                    .movieTitle("리바운드")
                    .phoneNumber("01012341234")
                    .startAt(LocalDateTime.now())
                    .endAt(LocalDateTime.now())
                    .ticketCount(3)
                    .build();
            //영화
            MovieEntity movieEntity = MovieEntity.builder()
                    .id(1L)
                    .title(ticketingRequest.getMovieTitle())
                    .playtime(123)
                    .description("하이")
                    .insertDate(LocalDateTime.now())
                    .build();

            //상영
            ScreenEntity screenEntity = ScreenEntity.builder()
                    .movieId(movieEntity.getId())
                    .price(123L)
                    .startAt(ticketingRequest.getStartAt())
                    .endAt(ticketingRequest.getStartAt().plusMinutes(movieEntity.getPlaytime()))
                    .build();

            List<ScreenEntity> screenEntities = Arrays.asList(
                    ScreenEntity.builder()
                            .movieId(movieEntity.getId()).startAt(screenEntity.getStartAt()).endAt(screenEntity.getEndAt())
                            .price(screenEntity.getPrice())
                            .build()
            );

            OrderDiscountEntity savedOrderDiscount = OrderDiscountEntity.builder()
                    .dayOfOrder(1)
                    .discountDate(screenEntity.getStartAt().toLocalDate())
                    .discountPolicy(DiscountPolicyEntity.builder()
                            .type(DiscountType.RATE.getValue())
                            .rate(1)
                            .price(0L)
                            .build())
                    .build();

            TicketingEntity ticketingEntity =TicketingEntity.builder()
                    .ticketId(LocalDateTime.now() +"_"+ ticketingRequest.getPhoneNumber())
                    .ticketCount(ticketingRequest.getTicketCount())
                    .status(Status.PENDING)
                    .price(screenEntity.getPrice()* ticketingRequest.getTicketCount())
                    .discountPrice(expectedDiscountPrice)
                    .build();

            when(movieEntityRepository.findByTitle(any())).thenReturn(Optional.ofNullable(movieEntity));
            when(screenEntityRepository.findByMovieIdAndStartAtAndEndAt(any(),any(),any()))
                    .thenReturn(Optional.ofNullable(screenEntity));
            when(screenEntityRepository.findAllByStartAtBetween(any(),any()))
                    .thenReturn(screenEntities);
            when(orderDiscountEntityRepository.findByDiscountDateAndDayOfOrder(any(),any()))
                    .thenReturn(Optional.ofNullable(savedOrderDiscount));
            when(ticketingEntityRepository.save(any()))
                    .thenReturn(ticketingEntity);
            Ticketing ticketing = ticketingService.reserve(ticketingRequest);

            Assertions.assertAll(
                    () -> assertEquals(expectedFinalPrice,ticketing.getPrice()),
                    () -> assertEquals(expectedDiscountPrice,ticketing.getDiscountPrice())
            );
        }

        @Test
        @DisplayName("티케팅 - 성공(티켓 최소금액 100원 / 티켓 가격 : 1000원, 티켓예매수 3개, 티켓 1장당 정률 99% 할인)")
        void ticketing_success_maximum_discount() {
            Long expectedFinalPrice = 100L;
            Long expectedDiscountPrice = 2900L;
            // given
            TicketingRequest ticketingRequest = TicketingRequest.builder()
                    .movieTitle("리바운드")
                    .phoneNumber("01012341234")
                    .startAt(LocalDateTime.now())
                    .endAt(LocalDateTime.now())
                    .ticketCount(3)
                    .build();
            //영화
            MovieEntity movieEntity = MovieEntity.builder()
                    .id(1L)
                    .title(ticketingRequest.getMovieTitle())
                    .playtime(123)
                    .description("하이")
                    .insertDate(LocalDateTime.now())
                    .build();

            //상영
            ScreenEntity screenEntity = ScreenEntity.builder()
                    .movieId(movieEntity.getId())
                    .price(1000L)
                    .startAt(ticketingRequest.getStartAt())
                    .endAt(ticketingRequest.getStartAt().plusMinutes(movieEntity.getPlaytime()))
                    .build();

            List<ScreenEntity> screenEntities = Arrays.asList(
                    ScreenEntity.builder()
                            .movieId(movieEntity.getId()).startAt(screenEntity.getStartAt()).endAt(screenEntity.getEndAt())
                            .price(screenEntity.getPrice())
                            .build()
            );

            OrderDiscountEntity savedOrderDiscount = OrderDiscountEntity.builder()
                    .dayOfOrder(1)
                    .discountDate(screenEntity.getStartAt().toLocalDate())
                    .discountPolicy(DiscountPolicyEntity.builder()
                            .type(DiscountType.RATE.getValue())
                            .rate(99)
                            .price(0L)
                            .build())
                    .build();

            TicketingEntity ticketingEntity =TicketingEntity.builder()
                    .ticketId(LocalDateTime.now() +"_"+ ticketingRequest.getPhoneNumber())
                    .ticketCount(ticketingRequest.getTicketCount())
                    .status(Status.PENDING)
                    .price(screenEntity.getPrice()* ticketingRequest.getTicketCount())
                    .discountPrice(expectedDiscountPrice)
                    .build();

            when(movieEntityRepository.findByTitle(any())).thenReturn(Optional.ofNullable(movieEntity));
            when(screenEntityRepository.findByMovieIdAndStartAtAndEndAt(any(),any(),any()))
                    .thenReturn(Optional.ofNullable(screenEntity));
            when(screenEntityRepository.findAllByStartAtBetween(any(),any()))
                    .thenReturn(screenEntities);
            when(orderDiscountEntityRepository.findByDiscountDateAndDayOfOrder(any(),any()))
                    .thenReturn(Optional.ofNullable(savedOrderDiscount));
            when(ticketingEntityRepository.save(any()))
                    .thenReturn(ticketingEntity);
            Ticketing ticketing = ticketingService.reserve(ticketingRequest);

            Assertions.assertAll(
                    () -> assertEquals(expectedFinalPrice,ticketing.getPrice()),
                    () -> assertEquals(expectedDiscountPrice,ticketing.getDiscountPrice())
            );
        }
    }

    @Nested
    @DisplayName("티케팅취소")
    class TicketCancel {
        @Test
        @DisplayName("티케팅취소 - 실패(티케팅 이력이 없다 -> RuntimeException)")
        void ticketing_cancel_fail_history_missing() {
            // given
            TicketingCancelRequest ticketingCancelRequest = TicketingCancelRequest.builder()
                    .ticketingId(1L)
                    .build();
            when(ticketingEntityRepository.findById(any())).thenThrow(new RuntimeException());

            // when, then
            assertThrows(RuntimeException.class, () -> ticketingService.cancel(ticketingCancelRequest.getTicketingId()));
            verify(ticketingEntityRepository, times(1)).findById(any());
        }

        @Test
        @DisplayName("티케팅취소 - 성공")
        void ticketing_cancel_success(){
            // given
            TicketingCancelRequest ticketingCancelRequest = TicketingCancelRequest.builder()
                    .ticketingId(1L)
                    .build();

            TicketingEntity ticketing = TicketingEntity.builder()
                    .id(1L)
                    .ticketCount(5)
                    .build();

            // when
            when(ticketingEntityRepository.findById(ticketingCancelRequest.getTicketingId())).thenReturn(Optional.ofNullable(ticketing));
            ticketingService.cancel(ticketingCancelRequest.getTicketingId());

            //then
            verify(ticketingEntityRepository,times(1)).findById(any());
        }
    }
}