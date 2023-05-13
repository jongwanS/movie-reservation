package com.jwcinema.discount.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.jwcinema.common.GlobalExceptionHandler;
import com.jwcinema.discount.application.DiscountService;
import com.jwcinema.discount.controller.dto.DiscountPolicyRequest;
import com.jwcinema.discount.controller.dto.DiscountType;
import com.jwcinema.discount.controller.dto.OrderDiscountRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DiscountController.class)
class DiscountControllerTest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private DiscountService discountService;

    @DisplayName("할인 등록 - 실패(할인 일자가 null)")
    @Test
    void register_fail_missingDate() throws Exception {
        OrderDiscountRequest orderDiscountRequest = OrderDiscountRequest.builder()
                .date(null)
                .dayOfOrder(1)
                .build();

        mvc.perform(post("/discount/order/register")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(orderDiscountRequest)))
                .andExpect(status().isBadRequest());

        verify(discountService, never()).register(orderDiscountRequest);
    }

    @DisplayName("할인 등록 - 실패(할인 순번이 null)")
    @Test
    void register_fail_missingDayOfOrder() throws Exception {
        OrderDiscountRequest orderDiscountRequest = OrderDiscountRequest.builder()
                .date(LocalDate.now().plusDays(1))
                .dayOfOrder(null)
                .build();

        mvc.perform(post("/discount/order/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(orderDiscountRequest)))
                .andExpect(status().isBadRequest());

        verify(discountService, never()).register(orderDiscountRequest);
    }

    @DisplayName("할인 등록 - 실패(할인 순번이 0보다 같거나 작다)")
    @Test
    void register_fail_underZeroDayOfOrder() throws Exception {
        OrderDiscountRequest orderDiscountRequest = OrderDiscountRequest.builder()
                .date(LocalDate.now().plusDays(1))
                .dayOfOrder(0)
                .build();

        mvc.perform(post("/discount/order/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(orderDiscountRequest)))
                .andExpect(status().isBadRequest());

        verify(discountService, never()).register(any());
    }

    @DisplayName("할인 등록 - 실패(할인 정책데이터가 존재하지 않음)")
    @Test
    void register_fail_missing_discountPolicy() throws Exception {
        OrderDiscountRequest orderDiscountRequest = OrderDiscountRequest.builder()
                .date(LocalDate.now().plusDays(1))
                .dayOfOrder(1)
                .policy(null)
                .build();

        mvc.perform(post("/discount/order/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(orderDiscountRequest)))
                .andExpect(status().isBadRequest());

        verify(discountService, never()).register(orderDiscountRequest);
    }

    @DisplayName("할인 등록 - 실패(할인정책 type(정률/정액)이 null)")
    @Test
    void register_fail_missing_discountPolicy_type() throws Exception {
        OrderDiscountRequest orderDiscountRequest = OrderDiscountRequest.builder()
                .date(LocalDate.now().plusDays(1))
                .dayOfOrder(1)
                .policy(DiscountPolicyRequest.builder()
                        .type(null)
                        .rate(0)
                        .price(0)
                        .build())
                .build();

        mvc.perform(post("/discount/order/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(orderDiscountRequest)))
                .andExpect(status().isBadRequest());

        verify(discountService, never()).register(any());
    }

    @DisplayName("할인 등록 - 실패(할인정책 type(정률/정액)이 'RATE' or 'FIX' 가 아닌경우)")
    @Test
    void register_fail_invalid_discountPolicy_type() throws Exception {
        OrderDiscountRequest orderDiscountRequest = OrderDiscountRequest.builder()
                .date(LocalDate.now().plusDays(1))
                .dayOfOrder(1)
                .policy(DiscountPolicyRequest.builder()
                        .type("UNKNOWN")
                        .rate(0)
                        .price(0)
                        .build())
                .build();

        mvc.perform(post("/discount/order/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(orderDiscountRequest)))
                .andExpect(status().isBadRequest());

        verify(discountService, never()).register(any());
    }

    @DisplayName("할인 등록 - 실패(할인정책 type(정률/정액)이 정률인데, 비율(rate)가 null인경우)")
    @Test
    void register_fail_discountPolicy_missing_rate() throws Exception {
        OrderDiscountRequest orderDiscountRequest = OrderDiscountRequest.builder()
                .date(LocalDate.now().plusDays(1))
                .dayOfOrder(1)
                .policy(DiscountPolicyRequest.builder()
                        .type(DiscountType.RATE.getValue())
                        .rate(0)
                        .price(0)
                        .build())
                .build();

        mvc.perform(post("/discount/order/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(orderDiscountRequest)))
                .andExpect(status().isBadRequest());

        verify(discountService, never()).register(any());
    }

    @DisplayName("할인 등록 - 실패(할인정책 type(정률/정액)이 정률인데, 할인비율(rate)이 0보다 작은경우)")
    @Test
    void register_fail_discountPolicy_invalid_rate_scope_minus() throws Exception {
        OrderDiscountRequest orderDiscountRequest = OrderDiscountRequest.builder()
                .date(LocalDate.now().plusDays(1))
                .dayOfOrder(1)
                .policy(DiscountPolicyRequest.builder()
                        .type(DiscountType.RATE.getValue())
                        .rate(0)
                        .price(0)
                        .build())
                .build();

        mvc.perform(post("/discount/order/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(orderDiscountRequest)))
                .andExpect(status().isBadRequest());

        verify(discountService, never()).register(any());
    }

    @DisplayName("할인 등록 - 실패(할인정책 type(정률/정액)이 정률인데, 할인비율(rate)이 100 이상인 경우)")
    @Test
    void register_fail_discountPolicy_invalid_rate_over_hundred() throws Exception {
        OrderDiscountRequest orderDiscountRequest = OrderDiscountRequest.builder()
                .date(LocalDate.now().plusDays(1))
                .dayOfOrder(1)
                .policy(DiscountPolicyRequest.builder()
                        .type(DiscountType.RATE.getValue())
                        .rate(100)
                        .price(0)
                        .build())
                .build();

        mvc.perform(post("/discount/order/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(orderDiscountRequest)))
                .andExpect(status().isBadRequest());

        verify(discountService, never()).register(any());
    }

    @DisplayName("할인 등록 - 실패(할인정책 type(정률/정액)이 정액인데, 할인가가 0인경우)")
    @Test
    void register_fail_discountPolicy_missing_price() throws Exception {
        OrderDiscountRequest orderDiscountRequest = OrderDiscountRequest.builder()
                .date(LocalDate.now().plusDays(1))
                .dayOfOrder(1)
                .policy(DiscountPolicyRequest.builder()
                        .type(DiscountType.FIX.getValue())
                        .rate(0)
                        .price(0)
                        .build())
                .build();

        mvc.perform(post("/discount/order/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(orderDiscountRequest)))
                .andExpect(status().isBadRequest());

        verify(discountService, never()).register(orderDiscountRequest);
    }

    @DisplayName("할인 등록 - 성공(정액할인)")
    @Test
    void register_success_fix_type() throws Exception {
        OrderDiscountRequest orderDiscountRequest = OrderDiscountRequest.builder()
                .date(LocalDate.now().plusDays(1))
                .dayOfOrder(1)
                .policy(DiscountPolicyRequest.builder()
                        .type(DiscountType.FIX.getValue())
                        .rate(0)
                        .price(1000L)
                        .build())
                .build();

        mvc.perform(post("/discount/order/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(orderDiscountRequest)))
                .andExpect(status().isOk());

        verify(discountService, times(1)).register(any());
    }
    @DisplayName("할인 등록 - 성공(정률할인)")
    @Test
    void register_success_rate_type() throws Exception {
        OrderDiscountRequest orderDiscountRequest = OrderDiscountRequest.builder()
                .date(LocalDate.now().plusDays(1))
                .dayOfOrder(1)
                .policy(DiscountPolicyRequest.builder()
                        .type(DiscountType.RATE.getValue())
                        .rate(10)
                        .price(0)
                        .build())
                .build();

        mvc.perform(post("/discount/order/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(orderDiscountRequest)))
                .andExpect(status().isOk());
        verify(discountService, times(1)).register(any());
    }
}