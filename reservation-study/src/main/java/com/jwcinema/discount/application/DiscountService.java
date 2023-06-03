package com.jwcinema.discount.application;

import com.jwcinema.discount.controller.dto.OrderDiscountRequest;
import com.jwcinema.discount.domain.*;
import com.jwcinema.discount.infra.DiscountPolicyEntityRepository;
import com.jwcinema.discount.infra.OrderDiscountEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final OrderDiscountEntityRepository orderDiscountEntityRepository;
    private final DiscountPolicyEntityRepository discountPolicyEntityRepository;

    public OrderDiscount register(OrderDiscountRequest orderDiscountRequest) {
        orderDiscountEntityRepository.findByDiscountDateAndDayOfOrder
                (orderDiscountRequest.getDate(), orderDiscountRequest.getDayOfOrder()).ifPresent(entity -> {
                    throw new DuplicateOrderDiscountException("중복된 순서할인이 이미 존재합니다.");
                });

        OrderDiscount orderDiscount = OrderDiscount.createDiscount(
                new DiscountId(orderDiscountRequest.getDayOfOrder(), orderDiscountRequest.getDate())
                ,DiscountPolicy.builder()
                        .price(orderDiscountRequest.getPolicy().getPrice())
                        .rate(orderDiscountRequest.getPolicy().getRate())
                        .type(orderDiscountRequest.getPolicy().getType())
                        .build()
        );

        OrderDiscountEntity discountEntity = orderDiscountEntityRepository.save(orderDiscount.toDiscountEntity());
        discountPolicyEntityRepository.save(orderDiscount.toPolicyEntity(discountEntity.getId()));

        return orderDiscount;
    }
}
