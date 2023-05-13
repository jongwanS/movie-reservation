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

        OrderDiscountEntity orderDiscountEntity = OrderDiscountEntity.builder()
                .discountDate(orderDiscountRequest.getDate())
                .dayOfOrder(orderDiscountRequest.getDayOfOrder())
                .build();
        OrderDiscountEntity savedDiscountEntity = orderDiscountEntityRepository.save(orderDiscountEntity);


        DiscountPolicyEntity savedDiscountPolicy = discountPolicyEntityRepository.save(
                DiscountPolicyEntity.builder()
                        .discountId(savedDiscountEntity.getId())
                        .type(orderDiscountRequest.getPolicy().getType())
                        .rate(orderDiscountRequest.getPolicy().getRate())
                        .price(orderDiscountRequest.getPolicy().getPrice())
                        .build()
        );

        return OrderDiscount.builder()
                .date(savedDiscountEntity.getDiscountDate())
                .dayOfOrder(savedDiscountEntity.getDayOfOrder())
                .policy(
                        DiscountPolicy.builder()
                                .price(savedDiscountPolicy.getPrice())
                                .rate(savedDiscountPolicy.getRate())
                                .type(savedDiscountPolicy.getType())
                                .build()
                )
                .build();
    }
}
