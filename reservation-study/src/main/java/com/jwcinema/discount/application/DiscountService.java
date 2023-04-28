package com.jwcinema.discount.application;

import com.jwcinema.discount.controller.OrderDiscountRequest;
import com.jwcinema.discount.controller.PeriodDiscountRequest;
import com.jwcinema.discount.domain.DiscountPolicyEntity;
import com.jwcinema.discount.domain.OrderDiscountEntity;
import com.jwcinema.discount.domain.PeriodDiscountEntity;
import com.jwcinema.discount.infra.DiscountPolicyEntityRepository;
import com.jwcinema.discount.infra.OrderDiscountEntityRepository;
import com.jwcinema.discount.infra.PeriodDiscountEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final PeriodDiscountEntityRepository periodDiscountEntityRepository;
    private final OrderDiscountEntityRepository orderDiscountEntityRepository;
    private final DiscountPolicyEntityRepository discountPolicyEntityRepository;

    public void register(PeriodDiscountRequest periodDiscountRequest) throws Exception {
        PeriodDiscountEntity periodDiscount = periodDiscountEntityRepository.save(
                PeriodDiscountEntity.builder()
                    .startAt(periodDiscountRequest.getStartAt())
                    .endAt(periodDiscountRequest.getEndAt())
                    .build());
        if(periodDiscount == null){
            throw new Exception("기간 할인 등록 실패");
        }

        DiscountPolicyEntity discountPolicy = discountPolicyEntityRepository.save(
                DiscountPolicyEntity.builder()
                        .discountId(periodDiscount.getId())
                        .type(periodDiscountRequest.getDiscount().getType())
                        .rate(periodDiscountRequest.getDiscount().getRate())
                        .price(periodDiscountRequest.getDiscount().getPrice())
                        .build()
        );
        if(discountPolicy == null){
            throw new Exception("기간 할인 '정책' 등록 실패");
        }

    }

    public void register(OrderDiscountRequest orderDiscountRequest) throws Exception {
        OrderDiscountEntity orderDiscount = orderDiscountEntityRepository.save(
                OrderDiscountEntity.builder()
                        .date(orderDiscountRequest.getDate())
                        .dayOfOrder(orderDiscountRequest.getDayOfOrder())
                        .build());
        if(orderDiscount == null){
            throw new Exception("순번 할인 등록 실패");
        }

        DiscountPolicyEntity discountPolicy = discountPolicyEntityRepository.save(
                DiscountPolicyEntity.builder()
                        .discountId(orderDiscount.getId())
                        .type(orderDiscountRequest.getDiscount().getType())
                        .rate(orderDiscountRequest.getDiscount().getRate())
                        .price(orderDiscountRequest.getDiscount().getPrice())
                        .build()
        );
        if(discountPolicy == null){
            throw new Exception("기간 할인 '정책' 등록 실패");
        }
    }
}
