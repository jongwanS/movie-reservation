package com.jwcinema.ticketing.application;

import com.jwcinema.movie.application.MovieService;
import com.jwcinema.movie.controller.dto.MovieRegisterRequest;
import com.jwcinema.movie.domain.MovieEntity;
import com.jwcinema.movie.infra.MovieEntityRepository;
import com.jwcinema.screen.domain.ScreenEntity;
import com.jwcinema.screen.infra.ScreenEntityRepository;
import com.jwcinema.ticketing.controller.dto.TicketingCancelRequest;
import com.jwcinema.ticketing.controller.dto.TicketingRequest;
import com.jwcinema.ticketing.domain.Status;
import com.jwcinema.ticketing.domain.TicketingEntity;
import com.jwcinema.ticketing.infra.TicketingEntityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TicketingServiceTest {
    @Mock
    private ScreenEntityRepository screenEntityRepository;
    @Mock
    private TicketingEntityRepository ticketingEntityRepository;

    @InjectMocks
    private TicketingService ticketingService;

    @Nested
    @DisplayName("티케팅")
    class TicketReserve {
        @Test
        @DisplayName("티케팅 - 실패(상영테이블에 등록된 영화가 없다 -> RuntimeException)")
        void ticketing_screen_missing() {
            // given
            TicketingRequest ticketingRequest = TicketingRequest.builder()
                    .screenId(1L)
                    .ticketCount(5)
                    .build();
            when(screenEntityRepository.findById(any())).thenThrow(new RuntimeException());

            // when, then
            assertThrows(RuntimeException.class, () -> ticketingService.reserve(ticketingRequest));
            verify(screenEntityRepository, times(1)).findById(any());
        }

        @Test
        @DisplayName("티케팅 - 성공")
        void ticketing_success(){
            // given
            TicketingRequest ticketingRequest = TicketingRequest.builder()
                    .screenId(1L)
                    .ticketCount(5)
                    .build();
            ScreenEntity screen = ScreenEntity.builder()
                    .movieId(1L)
                    .build();

            // when
            when(screenEntityRepository.findById(ticketingRequest.getScreenId())).thenReturn(Optional.ofNullable(screen));
            ticketingService.reserve(ticketingRequest);

            //then
            verify(screenEntityRepository, times(1)).findById(any());
            verify(ticketingEntityRepository,times(1)).save(any(TicketingEntity.class));
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