package com.jwcinema.discount.application;

import com.jwcinema.discount.controller.dto.DiscountPolicyRequest;
import com.jwcinema.discount.controller.dto.DiscountType;
import com.jwcinema.discount.controller.dto.OrderDiscountRequest;
import com.jwcinema.discount.domain.DiscountPolicyEntity;
import com.jwcinema.discount.domain.DuplicateOrderDiscountException;
import com.jwcinema.discount.domain.OrderDiscountEntity;
import com.jwcinema.discount.infra.DiscountPolicyEntityRepository;
import com.jwcinema.discount.infra.OrderDiscountEntityRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DiscountServiceTest {
    @Mock
    private OrderDiscountEntityRepository orderDiscountEntityRepository;

    @Mock
    private DiscountPolicyEntityRepository discountPolicyEntityRepository;

    @InjectMocks
    private DiscountService discountService;

    @Test
    @DisplayName("순서 할인 등록 - 실패(동일한 날짜(YYYY-MM-DD)의 동일한 순서 할인 등록은 불가하다.)")
    void register_fail_duplicated_day_and_order(){
        OrderDiscountRequest orderDiscountRequest = OrderDiscountRequest.builder()
                .dayOfOrder(1)
                .date(LocalDate.now())
                .build();

        OrderDiscountEntity savedOrderDiscount = OrderDiscountEntity.builder()
                .dayOfOrder(orderDiscountRequest.getDayOfOrder())
                .discountDate(orderDiscountRequest.getDate())
                .build();

        when(orderDiscountEntityRepository.findByDiscountDateAndDayOfOrder(any(),any()))
                .thenReturn(Optional.ofNullable(savedOrderDiscount));
        assertThrows(DuplicateOrderDiscountException.class, () -> discountService.register(orderDiscountRequest));
        verify(orderDiscountEntityRepository,times(1)).findByDiscountDateAndDayOfOrder(any(),any());
    }

    @Test
    @DisplayName("순서 할인 등록 - 성공")
    void register_success_order_discount() {

        //given
        OrderDiscountRequest orderDiscountRequest = OrderDiscountRequest.builder()
                .date(LocalDate.now().plusDays(1))
                .dayOfOrder(1)
                .policy(DiscountPolicyRequest.builder()
                        .type(DiscountType.FIX.getValue())
                        .rate(0)
                        .price(1000L)
                        .build())
                .build();


        OrderDiscountEntity savedOrderDiscountEntity = OrderDiscountEntity.builder()
                .dayOfOrder(orderDiscountRequest.getDayOfOrder())
                .discountDate(orderDiscountRequest.getDate())
                .build();

        DiscountPolicyEntity savedDiscountPolicy = DiscountPolicyEntity.builder()
                .discountId(savedOrderDiscountEntity.getId())
                .type(orderDiscountRequest.getPolicy().getType())
                .rate(orderDiscountRequest.getPolicy().getRate())
                .price(orderDiscountRequest.getPolicy().getPrice())
                .build();

        //when
        when(orderDiscountEntityRepository.findByDiscountDateAndDayOfOrder(any(),any())).thenReturn(Optional.empty());
        when(orderDiscountEntityRepository.save(any())).thenReturn(savedOrderDiscountEntity);
        when(discountPolicyEntityRepository.save(any())).thenReturn(savedDiscountPolicy);

        //then
        discountService.register(orderDiscountRequest);
        verify(orderDiscountEntityRepository, times(1)).findByDiscountDateAndDayOfOrder(any(),any());
        verify(orderDiscountEntityRepository, times(1)).save(any(OrderDiscountEntity.class));
        verify(discountPolicyEntityRepository, times(1)).save(any(DiscountPolicyEntity.class));
    }
}