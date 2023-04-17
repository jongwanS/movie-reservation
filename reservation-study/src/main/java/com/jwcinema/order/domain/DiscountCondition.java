package com.jwcinema.order.domain;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class DiscountCondition {
    private Long movieSeq;
    private Long movieTimeTableSeq;
    private DiscountConditionType type;
}
