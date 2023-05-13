package com.jwcinema.ticketing.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jwcinema.ticketing.application.TicketingService;
import com.jwcinema.ticketing.controller.dto.TicketingCancelRequest;
import com.jwcinema.ticketing.controller.dto.TicketingRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketingController.class)
class TicketingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TicketingService ticketingService;

    @Nested
    @DisplayName("티케팅")
    class TicketReserve {

        @DisplayName("티케팅 - 실패 (영화명이 null)")
        @Test
        void ticketing_fail_missing_movieTitle() throws Exception {
            // given
            TicketingRequest ticketingRequest = TicketingRequest.builder()
//                    .movieTitle("리바운드")
                    .phoneNumber("01012341234")
                    .startAt(LocalDateTime.now())
                    .endAt(LocalDateTime.now())
                    .ticketCount(3)
                    .build();

            // when
            mockMvc.perform(post("/ticketing/reserve")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(ticketingRequest)))
                    .andExpect(status().isBadRequest());

            // then
            verify(ticketingService, never()).reserve(ticketingRequest);
        }
        @DisplayName("티케팅 - 실패 (휴대폰 번호가 빈값)")
        @Test
        void ticketing_fail_missing_screenId() throws Exception {
            // given
            TicketingRequest ticketingRequest = TicketingRequest.builder()
                    .movieTitle("리바운드")
                    .phoneNumber("")
                    .startAt(LocalDateTime.now())
                    .endAt(LocalDateTime.now())
                    .ticketCount(3)
                    .build();

            // when
            mockMvc.perform(post("/ticketing/reserve")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(ticketingRequest)))
                    .andExpect(status().isBadRequest());

            // then
            verify(ticketingService, never()).reserve(ticketingRequest);
        }
        @DisplayName("티케팅 - 실패 (예매 티켓 갯수가 0개)")
        @Test
        void ticketing_fail_missing_ticketCount() throws Exception {
            // given
            TicketingRequest ticketingRequest = TicketingRequest.builder()
                    .movieTitle("리바운드")
                    .phoneNumber("01012341234")
                    .startAt(LocalDateTime.now())
                    .endAt(LocalDateTime.now())
                    .ticketCount(0)
                    .build();

            // when
            mockMvc.perform(post("/ticketing/reserve")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(ticketingRequest)))
                    .andExpect(status().isBadRequest());

            // then
            verify(ticketingService, never()).reserve(ticketingRequest);
        }
        @DisplayName("티케팅 - 실패 (예매 티켓 갯수가 음수)")
        @Test
        void ticketing_fail_zero_ticketCount() throws Exception {
            // given
            TicketingRequest ticketingRequest = TicketingRequest.builder()
                    .movieTitle("리바운드")
                    .phoneNumber("01012341234")
                    .startAt(LocalDateTime.now())
                    .endAt(LocalDateTime.now())
                    .ticketCount(-1)
                    .build();

            // when
            mockMvc.perform(post("/ticketing/reserve")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(ticketingRequest)))
                    .andExpect(status().isBadRequest());

            // then
            verify(ticketingService, never()).reserve(ticketingRequest);
        }
        @DisplayName("티케팅 - 성공")
        @Test
        void ticketing_success() throws Exception {
            // given
            TicketingRequest ticketingRequest = TicketingRequest.builder()
                    .movieTitle("리바운드")
                    .phoneNumber("01012341234")
                    .startAt(LocalDateTime.now())
                    .endAt(LocalDateTime.now())
                    .ticketCount(3)
                    .build();

            // when
            mockMvc.perform(post("/ticketing/reserve")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(ticketingRequest)))
                    .andExpect(status().isOk());

            // then
            verify(ticketingService, times(1)).reserve(any());
        }
    }
    @Nested
    @DisplayName("티케팅 취소")
    class TicketCancel {
        @DisplayName("티케팅취소 - 실패(취소할 ticketingId 는 필수값)")
        @Test
        void ticketing_cancel_fail_missing_ticketingId() throws Exception {
            // given
            TicketingCancelRequest ticketingCancelRequest = TicketingCancelRequest.builder()
                    .ticketingId(null)
                    .build();

            // when
            mockMvc.perform(post("/ticketing/cancel")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(ticketingCancelRequest)))
                    .andExpect(status().isBadRequest());

            // then
            verify(ticketingService, never()).cancel(any());
        }

        @DisplayName("티케팅 취소 - 성공")
        @Test
        void ticketing_cancel_success() throws Exception {
            // given
            TicketingCancelRequest ticketingCancelRequest = TicketingCancelRequest.builder()
                    .ticketingId(1L)
                    .build();

            // when
            mockMvc.perform(post("/ticketing/cancel")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(ticketingCancelRequest)))
                    .andExpect(status().isOk());

            // then
            verify(ticketingService, times(1)).cancel(any());
        }
    }
}