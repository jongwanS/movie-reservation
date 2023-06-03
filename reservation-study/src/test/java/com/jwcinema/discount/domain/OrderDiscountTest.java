package com.jwcinema.discount.domain;

import com.jwcinema.common.InvalidParameterException;
import com.jwcinema.discount.controller.dto.DiscountType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

public class OrderDiscountTest {

    @Test
    @DisplayName("정률 20% 할인 계산")
    public void testCalculateDiscountPrice_WithRateDiscountType_ShouldReturnDiscountedPrice() {
        // Arrange
        DiscountId discountId = DiscountId.builder()
                .dayOfOrder(1)
                .date(LocalDate.now())
                .build();

        DiscountPolicy discountPolicy = DiscountPolicy.builder()
                .type(DiscountType.RATE.getValue())
                .rate(20)
                .build();

        OrderDiscount orderDiscount = OrderDiscount.builder()
                .id(discountId)
                .policy(discountPolicy)
                .build();

        long screenPrice = 10000L;

        // Act
        long discountedPrice = orderDiscount.calculateDiscountPrice(screenPrice);

        // Assert
        long expectedDiscountedPrice = 8000L; // 20% discount on 10000L
        Assertions.assertEquals(expectedDiscountedPrice, screenPrice - discountedPrice);
    }

    @Test
    @DisplayName("정액 2000원 할인")
    public void testCalculateDiscountPrice_WithFixDiscountType_ShouldReturnDiscountedPrice() {
        // Arrange
        DiscountId discountId = DiscountId.builder()
                .dayOfOrder(1)
                .date(LocalDate.now())
                .build();

        DiscountPolicy discountPolicy = DiscountPolicy.builder()
                .type(DiscountType.FIX.getValue())
                .price(2000L)
                .build();

        OrderDiscount orderDiscount = OrderDiscount.builder()
                .id(discountId)
                .policy(discountPolicy)
                .build();

        long screenPrice = 10000L;

        // Act
        long discountedPrice = orderDiscount.calculateDiscountPrice(screenPrice);

        // Assert
        long expectedDiscountedPrice = 8000L; // 10000L - 2000L
        Assertions.assertEquals(expectedDiscountedPrice, discountedPrice);
    }

    @Test
    public void testValidate_WithInvalidDiscountId_ShouldThrowInvalidParameterException() {
        // Arrange
        DiscountId discountId = DiscountId.builder()
                .dayOfOrder(0) // Invalid dayOfOrder value
                .date(null) // Invalid date value
                .build();

        DiscountPolicy discountPolicy = DiscountPolicy.builder()
                .type(DiscountType.RATE.getValue())
                .rate(20)
                .build();

        // Act & Assert
        Assertions.assertThrows(InvalidParameterException.class, () -> {
            OrderDiscount.builder()
                    .id(discountId)
                    .policy(discountPolicy)
                    .build();
        });
    }

    @Test
    @DisplayName("존재하지 않는 할인정책")
    public void testValidate_WithInvalidDiscountPolicy_ShouldThrowInvalidParameterException() {
        // Arrange
        DiscountId discountId = DiscountId.builder()
                .dayOfOrder(1)
                .date(LocalDate.now())
                .build();

        // Act & Assert
        Assertions.assertThrows(InvalidParameterException.class, () -> {
            DiscountPolicy discountPolicy = DiscountPolicy.builder()
                    .type("InvalidType") // Invalid discount type
                    .rate(20)
                    .build();
        });
    }
}