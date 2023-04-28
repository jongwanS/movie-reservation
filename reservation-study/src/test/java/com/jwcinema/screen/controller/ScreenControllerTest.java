package com.jwcinema.screen.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jwcinema.screen.application.ScreenService;
import com.jwcinema.screen.controller.dto.ScreenRegisterRequest;
import org.junit.jupiter.api.DisplayName;
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

@WebMvcTest(ScreenController.class)
class ScreenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ScreenService screenService;

    @DisplayName("상영 등록 - 실패 (movieId 가 null)")
    @Test
    void register_fail_missingName() throws Exception {
        // given
        ScreenRegisterRequest request = ScreenRegisterRequest.builder()
                .movieId(null)
                .price(1000L)
                .startAt(LocalDateTime.now())
                .endAt(LocalDateTime.now())
                .build();

        // when
        mockMvc.perform(post("/screen/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        // then
        verify(screenService, never()).register(request);
    }

    @DisplayName("상영 등록 - 실패 (price 가 null 인 경우)")
    @Test
    void register_fail_missingPrice() throws Exception {
        // given
        ScreenRegisterRequest request = ScreenRegisterRequest.builder()
                .movieId(1L)
                .price(null)
                .startAt(LocalDateTime.now())
                .endAt(LocalDateTime.now())
                .build();

        // when
        mockMvc.perform(post("/screen/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        // then
        verify(screenService, never()).register(request);
    }

    @DisplayName("상영 등록 - 실패 (startAt 가 null 인 경우)")
    @Test
    void register_fail_missingStartAt() throws Exception {
        // given
        ScreenRegisterRequest request = ScreenRegisterRequest.builder()
                .movieId(1L)
                .price(null)
                .startAt(null)
                .endAt(LocalDateTime.now())
                .build();

        // when
        mockMvc.perform(post("/screen/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        // then
        verify(screenService, never()).register(request);
    }
    @DisplayName("상영 등록 - 실패 (startAt 가 null 인 경우)")
    @Test
    void register_fail_missingEndAt() throws Exception {
        // given
        ScreenRegisterRequest request = ScreenRegisterRequest.builder()
                .movieId(1L)
                .price(null)
                .startAt(LocalDateTime.now())
                .endAt(null)
                .build();

        // when
        mockMvc.perform(post("/screen/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(request)))
                .andExpect(status().isBadRequest());

        // then
        verify(screenService, never()).register(request);
    }

    @DisplayName("상영 등록시, screenService 의 register 메서드를 호출한다.")
    @Test
    void register_service_call() throws Exception {
        // given
        ScreenRegisterRequest request = ScreenRegisterRequest.builder()
                .movieId(1L)
                .price(1000L)
                .startAt(LocalDateTime.now())
                .endAt(LocalDateTime.now())
                .build();

        // when
        mockMvc.perform(post("/screen/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(request)))
                .andExpect(status().isOk());

        // then
        verify(screenService, times(1)).register(any());
    }

    @DisplayName("상영 등록 - 성공")
    @Test
    void register_success() throws Exception {
        // given
        ScreenRegisterRequest request = ScreenRegisterRequest.builder()
                .movieId(1L)
                .price(1000L)
                .startAt(LocalDateTime.now())
                .endAt(LocalDateTime.now())
                .build();

        // when
        mockMvc.perform(post("/screen/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}