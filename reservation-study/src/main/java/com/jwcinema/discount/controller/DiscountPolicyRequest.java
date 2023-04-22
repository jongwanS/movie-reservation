package com.jwcinema.discount.controller;

import lombok.Builder;
import lombok.Getter;
import org.springframework.util.ObjectUtils;

@Getter
public class DiscountPolicyRequest {
    private String type;
    private String rate;
    private Long price;

    @Builder
    public DiscountPolicyRequest(String type, String rate, Long price) throws Exception {
        this.type = type;
        this.rate = rate;
        this.price = price;
        validate();
    }
    private void validate() throws Exception {
        if(ObjectUtils.isEmpty(type)){
            throw new Exception("type 필수");
        }
        if(!DiscountType.contains(type)){
            throw new Exception("잘못된 할인 타입 type ::"+ type);
        }
        if(ObjectUtils.isEmpty(rate)){
            throw new Exception("rate 필수");
        }
        if(ObjectUtils.isEmpty(price)){
            throw new Exception("price 필수");
        }
    }
}
