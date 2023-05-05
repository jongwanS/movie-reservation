package com.jwcinema.discount.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DiscountPolicy {

    private String type;
    private Integer rate;
    private Long price;
}
