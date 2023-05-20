package com.jwcinema.discount.infra;

import com.jwcinema.discount.domain.OrderDiscount;

import java.time.LocalDate;
import java.util.Optional;

public interface OrderDiscountEntityRepositoryImpl {
    Optional<OrderDiscount> findByDiscountDateAndDayOfOrder(LocalDate discountDate, Integer dayOfOrder);
}
