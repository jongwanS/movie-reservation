package com.movie.order.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DiscountPolicy {
    private Long movieSeq;
    private Long movieTimeTableSeq;
    private Integer rate;
    private Integer money;
    private DiscountPolicyType type;
}
